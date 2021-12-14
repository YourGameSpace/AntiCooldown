package com.yourgamespace.anticooldown.utils.module;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.AntiCooldownLogger;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.InvalidDescriptionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"ForLoopReplaceableByForEach", "rawtypes", "unchecked", "MismatchedQueryAndUpdateOfCollection", "ResultOfMethodCallIgnored"})
public class ModuleHandler {

    private final AntiCooldownLogger antiCooldownLogger = AntiCooldown.getAntiCooldownLogger();
    private final Data data = AntiCooldown.getData();

    public ModuleHandler() {}

    private final ArrayList<AntiCooldownModule> enabledModules = new ArrayList<>();
    private final Map<String, Class<?>> classes = new ConcurrentHashMap();
    private final List<ModuleClassLoader> loaders = new CopyOnWriteArrayList();
    private final Pattern pattern = Pattern.compile("\\.jar$");
    private File folder;

    public ArrayList<AntiCooldownModule> getEnabledModules() {
        return enabledModules;
    }

    public AntiCooldownModule getModule(String name) {
        for (AntiCooldownModule antiCooldownModule : enabledModules) {
            if (antiCooldownModule.getDescription().getName().equalsIgnoreCase(name)) return antiCooldownModule;
        }
        return null;
    }

    public void registerModule(AntiCooldownModule antiCooldownModule) {
        // If ProtocolLib is required: Check if installed
        if (antiCooldownModule.isProtocolLibRequired() && !data.isProtocolLibInstalled()) {
            antiCooldownLogger.warn("§cModule §e" + antiCooldownModule.getDescription().getName() + " §crequires §eProtocolLib §cto be installed!");
            return;
        }

        // If compatibility test failed: Do not enable module
        if (!antiCooldownModule.compatibilityTest()) {
            antiCooldownLogger.warn("§cCompatibility-Test failed! Module §e" + antiCooldownModule.getDescription().getName() + " §cwill not be enabled!");
            return;
        }

        antiCooldownModule.enableModule();
        enabledModules.add(antiCooldownModule);
    }

    public void unregisterModule(String moduleName) {
        for (Iterator<AntiCooldownModule> antiCooldownModuleIterator = enabledModules.iterator(); antiCooldownModuleIterator.hasNext(); ) {
            AntiCooldownModule antiCooldownModule = antiCooldownModuleIterator.next();

            if (!antiCooldownModule.getDescription().getName().equals(moduleName)) continue;
            enabledModules.remove(antiCooldownModule);
            antiCooldownModule.disableModule();
        }
    }

    public void unregisterModule(String moduleName, String reason) {
        for (Iterator<AntiCooldownModule> antiCooldownModuleIterator = enabledModules.iterator(); antiCooldownModuleIterator.hasNext(); ) {
            AntiCooldownModule antiCooldownModule = antiCooldownModuleIterator.next();

            if (!antiCooldownModule.getDescription().getName().equals(moduleName)) continue;
            enabledModules.remove(antiCooldownModule);
            antiCooldownModule.disableModule(reason);
        }
    }

    public void unregisterAllModules() {
        for (Iterator<AntiCooldownModule> antiCooldownModuleIterator = enabledModules.iterator(); antiCooldownModuleIterator.hasNext(); ) {
            AntiCooldownModule antiCooldownModule = antiCooldownModuleIterator.next();

            antiCooldownModule.disableModule();
            antiCooldownModuleIterator.remove();
        }
    }

    public void enableModules() {
        checkFolder();

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            try {
                AntiCooldownModule antiCooldownModule = loadModule(file);
                if (antiCooldownModule != null) {
                    registerModule(antiCooldownModule);
                }
            } catch (ModuleException e) {
                e.printStackTrace();
            }
        }
    }

    public void enableModule(File file) {
        checkFolder();

        try {
            AntiCooldownModule antiCooldownModule = loadModule(file);
            if (antiCooldownModule != null) {
                registerModule(antiCooldownModule);
            }
        } catch (ModuleException e) {
            e.printStackTrace();
        }
    }

    private void checkFolder() {
        folder = new File( "plugins/AntiCooldown/modules/");
        if (!folder.exists()) folder.mkdir();
    }

    private ModuleDescription loadDescription(File file) throws ModuleException {
        ModuleDescription description;
        JarFile jar;
        InputStream inputStream;

        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("module.yml");
            if (entry == null) {
                throw new ModuleException(new FileNotFoundException("Module '" + file.getName() + "' does not contain module.yml"));
            }

            inputStream = jar.getInputStream(entry);
            description = new ModuleDescription(inputStream);
        } catch (IOException | InvalidDescriptionException exception) {
            throw new ModuleException(exception);
        }

        return description;
    }

    private AntiCooldownModule loadModule(File file) throws ModuleException {
        // Check if files exists
        if (!file.exists()) {
            throw new ModuleException(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        // Check if jar file
        Matcher matcher = pattern.matcher(file.getName());
        if (!matcher.find()) {
            return null;
        }

        ModuleDescription description = loadDescription(file);
        Class pluginClass;
        Class jarClass;

        // Get main class
        try {
            jarClass = Class.forName(description.getMain(), true, new ModuleClassLoader(file, getClass().getClassLoader(), description));
        } catch (ClassNotFoundException | IOException exception) {
            throw new ModuleException(exception, "Cannot find main class `" + description.getMain() + "' for module '" + file.getName() + "'");
        }

        // Validate main and return
        try {
            try {
                pluginClass = jarClass.asSubclass(AntiCooldownModule.class);
            } catch (ClassCastException exception) {
                throw new ModuleException(exception, "Main class `" + description.getMain() + "' of module '" + file.getName() + "' does not extend AntiCooldownModule");
            }
            AntiCooldownModule antiCooldownModule = (AntiCooldownModule) pluginClass.getConstructor(boolean.class).newInstance(false);
            antiCooldownModule.setDescription(description);
            return antiCooldownModule;
        } catch (IllegalAccessException exception) {
            throw new ModuleException(exception, "No public constructor");
        } catch (InstantiationException exception) {
            throw new ModuleException(exception, "Abnormal module type");
        } catch (InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<?> getClassByName(String name) {
        Class<?> cachedClass = classes.get(name);
        if (cachedClass != null) {
            return cachedClass;
        }

        for (ModuleClassLoader loader : loaders) {
            try {
                cachedClass = loader.findClass(name, false);
            } catch (ClassNotFoundException classNotFoundException) {
                // Just catch
            }
        }
        return cachedClass;
    }

    public void setClass(String name, Class<?> paramClass) {
        if (!classes.containsKey(name)) {
            classes.put(name, paramClass);
            if (ConfigurationSerializable.class.isAssignableFrom(paramClass)) {
                Class<? extends ConfigurationSerializable> serializable = paramClass.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.registerClass(serializable);
            }
        }
    }
}
