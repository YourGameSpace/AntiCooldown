package com.yourgamespace.anticooldown.utils.module;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModuleLoader {

    private static final ModuleHandler moduleHandler = AntiCooldown.getModuleHandler();

    private static final Map<String, Class<?>> classes = new ConcurrentHashMap();
    private static final List<ModuleClassLoader> loaders = new CopyOnWriteArrayList();
    private static File folder;
    private static Pattern pattern = Pattern.compile("\\.jar$");

    public static void load() {
        checkFolder();

        for (File file : folder.listFiles()) {
            try {
                long millis = System.currentTimeMillis();
                AntiCooldownModule m = loadModule(file);
                if (m != null) {
                    moduleHandler.registerModule(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkFolder() {
        folder = new File(AntiCooldown.getInstance().getDataFolder() + "/modules/");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    private static AntiCooldownModule loadModule(File file) throws ModuleException {
        Validate.notNull(file, "File cannot be null");
        if (!file.exists()) {
            throw new ModuleException(new FileNotFoundException(file.getPath() + " does not exist"));
        }
        Matcher matcher = pattern.matcher(file.getName());
        if (!matcher.find()) {
            return null;
        }
        ModuleDescription description = loadDescription(file);
        try {
            Class pluginClass;
            Class jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, new ModuleClassLoader(file, ModuleLoader.class.getClassLoader(), description));
            } catch (ClassNotFoundException e) {
                throw new ModuleException(pluginClass, "Cannot find main class `" + description.getMain() + "'");
            } catch (Throwable e) {
                throw new ModuleException(pluginClass);
            }


            try {
                pluginClass = jarClass.asSubclass(AntiCooldownModule.class);
            } catch (ClassCastException e) {
                throw new ModuleException(e, "Main class `" + description.getMain() + "' does not extend Module");
            }
            AntiCooldownModule m = (AntiCooldownModule) pluginClass.newInstance();
            m.setDescription(description);
            return m;
        } catch (IllegalAccessException e) {
            Class jarClass; throw new ModuleException(jarClass, "No public constructor");
        } catch (InstantiationException e) {
            throw new ModuleException(e, "Abnormal module type");
        }
    }

    private static ModuleDescription loadDescription(File file) throws ModuleException {
        ModuleDescription description;
        Validate.notNull(file, "File cannot be null");
        jar = null;
        stream = null;


        try {
            jar = new JarFile(file);
            entry = jar.getJarEntry("module.yml");
            if (entry == null) {
                throw new ModuleException(new FileNotFoundException("Jar does not contain module.yml"));
            }

            stream = jar.getInputStream(entry);
            description = new ModuleDescription(stream);
        } catch (IOException e) {
            throw new ModuleException(e);
        } catch (InvalidDescriptionException e) {
            throw new ModuleException(e);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                    ExceptionLogger.log(e, "Could not close JarFile");
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    ExceptionLogger.log(e, "Could not close Stream");
                }
            }
        }
        return description;
    }

    static Class<?> getClassByName(String name) {
        Class<?> cachedClass = (Class)classes.get(name);
        if (cachedClass != null) {
            return cachedClass;
        }
        Iterator it = loaders.iterator();

        while (it.hasNext()) {
            ModuleClassLoader loader = (ModuleClassLoader)it.next();

            try {
                cachedClass = loader.findClass(name, false);
            } catch (ClassNotFoundException classNotFoundException) {}


            if (cachedClass != null) {
                return cachedClass;
            }
        }

        return null;
    }


    static void setClass(String name, Class<?> clazz) {
        if (!classes.containsKey(name)) {
            classes.put(name, clazz);
            if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.registerClass(serializable);
            }
        }
    }
}
