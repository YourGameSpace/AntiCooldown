package com.yourgamespace.anticooldown.utils.module;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ModuleConfig {

    private final AntiCooldownModule antiCooldownModule;
    private final String configSuffix;
    private final String configPrefix;

    public ModuleConfig(@NotNull AntiCooldownModule antiCooldownModule, @Nullable String configPrefix, @Nullable String configSuffix) {
        this.antiCooldownModule = antiCooldownModule;
        this.configPrefix = configPrefix;
        this.configSuffix = configSuffix;
    }

    private File configFile;
    private FileConfiguration fileConfiguration;

    public final File getFile() {
        // If null then assign
        if (configFile == null) {
            configFile = new File("plugins/AntiCooldown", "module-" + configPrefix + antiCooldownModule.getDescription().getName().toLowerCase() + configSuffix + ".yml");
        }

        // Create file if not exists
        try {
            configFile.createNewFile();
        } catch (IOException exception) {
            antiCooldownModule.getLogger().warn("§cAn error occurred while trying to create new module config:");
            exception.printStackTrace();
        }

        return configFile;
    }

    public final FileConfiguration getConfig() {
        // If null then assign
        if (fileConfiguration == null) {
            fileConfiguration = YamlConfiguration.loadConfiguration(getFile());
        }

        return fileConfiguration;
    }

    public final void saveConfig() {
        try {
            getConfig().save(getFile());
        } catch (IOException exception) {
            antiCooldownModule.getLogger().warn("§cAn error occurred while trying to save module config changes:");
            exception.printStackTrace();
        }
    }
}
