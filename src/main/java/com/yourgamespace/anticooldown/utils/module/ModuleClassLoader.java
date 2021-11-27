package com.yourgamespace.anticooldown.utils.module;

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModuleClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    private final JarFile jar;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap();
    private final ModuleDescription description;
    private final Manifest manifest;
    private final URL url;

    ModuleClassLoader(File file, ClassLoader parent, ModuleDescription description) throws IOException {
        super(new URL[]{file.toURI().toURL()}, parent);
        this.description = description;
        this.jar = new JarFile(file);
        this.manifest = this.jar.getManifest();
        this.url = file.toURI().toURL();
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (!name.startsWith("org.bukkit.") && !name.startsWith("net.minecraft.")) {
            Class<?> result = (Class) this.classes.get(name);
            if (result == null) {
                if (checkGlobal) {
                    result = ModuleLoader.getClassByName(name);
                }

                if (result == null) {
                    String path = name.replace('.', '/').concat(".class");
                    JarEntry entry = this.jar.getJarEntry(path);
                    if (entry != null) {
                        byte[] classBytes;

                        try {
                            InputStream is = this.jar.getInputStream(entry);

                            try {
                                classBytes = ByteStreams.toByteArray(is);
                            } finally {
                                if (is != null) {
                                    is.close();
                                }
                            }

                        } catch (IOException e) {
                            throw new ClassNotFoundException(name, e);
                        }

                        byte[] classBytes = processClass(this.description, path, classBytes);
                        int dot = name.lastIndexOf('.');
                        if (dot != -1) {
                            String pkgName = name.substring(0, dot);
                            if (getPackage(pkgName) == null) {
                                try {
                                    if (this.manifest != null) {
                                        definePackage(pkgName, this.manifest, this.url);
                                    } else {
                                        definePackage(pkgName, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (URL) null);
                                    }
                                } catch (IllegalArgumentException e) {
                                    if (getPackage(pkgName) == null) {
                                        throw new IllegalStateException("Cannot find package " + pkgName);
                                    }
                                }
                            }
                        }

                        CodeSigner[] signers = entry.getCodeSigners();
                        CodeSource source = new CodeSource(this.url, signers);
                        result = defineClass(name, classBytes, 0, classBytes.length, source);
                    }

                    if (result == null) {
                        result = super.findClass(name);
                    }

                    if (result != null) {
                        ModuleLoader.setClass(name, result);
                    }
                }

                this.classes.put(name, result);
            }

            return result;
        }
        throw new ClassNotFoundException(name);
    }


    private byte[] processClass(ModuleDescription description, String path, byte[] clazz) {
        return clazz;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.jar.close();
        }
    }


    Set<String> getClasses() {
        return this.classes.keySet();
    }
}