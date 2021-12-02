package com.yourgamespace.anticooldown.utils.module;

import com.google.common.io.ByteStreams;
import com.yourgamespace.anticooldown.main.AntiCooldown;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

@SuppressWarnings({"UnstableApiUsage", "unchecked", "rawtypes", "FieldCanBeLocal"})
public class ModuleClassLoader extends URLClassLoader {

    private final ModuleHandler moduleHandler = AntiCooldown.getModuleHandler();

    ModuleClassLoader(File file, ClassLoader parent, ModuleDescription description) throws IOException {
        super(new URL[] { file.toURI().toURL() }, parent);
        this.description = description;
        this.jar = new JarFile(file);
        this.manifest = this.jar.getManifest();
        this.url = file.toURI().toURL();
    }

    private final JarFile jar;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap();
    private final ModuleDescription description;
    private final Manifest manifest;
    private final URL url;

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (!name.startsWith("org.bukkit.") && !name.startsWith("net.minecraft.")) {

            // Check cache and return cached result if found
            Class<?> result = classes.get(name);
            if (result != null) return result;

            // Check ModuleLoader cache and put to local cache and return if found
            if (checkGlobal) {
                result = moduleHandler.getClassByName(name);
                if (result != null) {
                    classes.put(name, result);
                    return result;
                }
            }

            String path = name.replace('.', '/').concat(".class");
            JarEntry entry = jar.getJarEntry(path);

            if (entry == null) {
                if (result == null) {
                    result = findClass(name);
                } else {
                    moduleHandler.setClass(name, result);
                    classes.put(name, result);
                }
            }

            byte[] classBytes;
            try {
                InputStream inputStream = jar.getInputStream(entry);
                try {
                    classBytes = ByteStreams.toByteArray(inputStream);
                } finally {
                    if (inputStream != null) inputStream.close();
                }
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }

            int dot = name.lastIndexOf('.');
            if (dot != -1) {
                String pkgName = name.substring(0, dot);
                if (getPackage(pkgName) == null) {
                    try {
                        if (this.manifest != null) {
                            definePackage(pkgName, this.manifest, this.url);
                        } else {
                            definePackage(pkgName, null, null, null, null, null, null, null);
                        }
                    } catch (IllegalArgumentException e) {
                        if (getPackage(pkgName) == null) {
                            throw new IllegalStateException("Cannot find package " + pkgName);
                        }
                    }
                }
            }

            CodeSigner[] signers = Objects.requireNonNull(entry).getCodeSigners();
            CodeSource source = new CodeSource(this.url, signers);
            result = defineClass(name, classBytes, 0, classBytes.length, source);

            if (result != null) {
                moduleHandler.setClass(name, result);
                return result;
            } else {
                return findClass(name, true);
            }
        }

        // No match
        throw new ClassNotFoundException(name);
    }

    Set<String> getClasses() {
        return classes.keySet();
    }
}
