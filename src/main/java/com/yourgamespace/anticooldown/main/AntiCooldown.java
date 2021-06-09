package com.yourgamespace.anticooldown.main;

import com.yourgamespace.anticooldown.commands.CmdAntiCooldown;
import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.files.PluginConfig;
import com.yourgamespace.anticooldown.listener.Join;
import com.yourgamespace.anticooldown.listener.Quit;
import com.yourgamespace.anticooldown.listener.SweepAttack;
import com.yourgamespace.anticooldown.listener.SwitchWorld;
import com.yourgamespace.anticooldown.utils.Metrics;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import de.tubeof.tubetils.api.updatechecker.UpdateChecker;
import de.tubeof.tubetils.api.updatechecker.enums.ApiMethode;
import de.tubeof.tubetilsmanager.TubeTilsManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class AntiCooldown extends JavaPlugin {

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private static AntiCooldown main;
    private static TubeTilsManager tubeTilsManager;
    private static CacheContainer cacheContainer;
    private static UpdateChecker updateChecker;
    private static Data data;
    private static PluginConfig pluginConfig;

    @Override
    public void onEnable() {
        initialisation();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe Plugin will be activated ...");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "==================================================");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "JOIN MY DISCORD OUR: §ehttps://discord.gg/73ZDfbx");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "==================================================");

        manageConfigs();
        registerListener();
        registerCommands();
        checkUpdate();

        setOnlinePlayersCooldown();
        bStats();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe plugin was successfully activated!");
    }

    @Override
    public void onDisable() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe Plugin will be deactivated ...");

        setDefaultCooldown();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe plugin was successfully deactivated!");
    }

    private void initialisation() {
        main = this;

        tubeTilsManager = new TubeTilsManager("§7[§3AntiCooldownLogger§7] ", this, "SNAPSHOT-47", "1.0.2", true);
        cacheContainer = new CacheContainer("AntiCooldown");
        cacheContainer.registerCacheType(String.class);
        cacheContainer.registerCacheType(Boolean.class);
        cacheContainer.registerCacheType(Integer.class);
        cacheContainer.add(String.class, "STARTUP_PREFIX", "§7[§3AntiCooldownLogger§7] ");

        data = new Data();
        pluginConfig = new PluginConfig();
    }

    private void manageConfigs() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aLoading Config Files ...");

        pluginConfig.cfgConfig();
        pluginConfig.setCache();
        if(data.getConfigVersion() != 7) pluginConfig.configUpdateMessage();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aConfig Files was successfully loaded!");
    }

    private void registerListener() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aListeners will be registered ...");

        pluginManager.registerEvents(new Join(), this);
        pluginManager.registerEvents(new Quit(), this);
        pluginManager.registerEvents(new SwitchWorld(), this);
        pluginManager.registerEvents(new SweepAttack(), this);
        //pluginManager.registerEvents(new ItemRestriction(), this);

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aListeners have been successfully registered!");
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aCommands will be registered ...");

        getCommand("anticooldown").setExecutor(new CmdAntiCooldown());

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aCommands have been successfully registered!");
    }

    private void checkUpdate() {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_UPDATE_CHECKER"))) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cCheck for updates disabled. The check will be skipped!");
            return;
        }

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aChecking for updates ...");
        try {
            updateChecker = new UpdateChecker(51321, this, ApiMethode.YOURGAMESPACE, false);
            if(updateChecker.isOutdated()) {
                if(ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "UPDATE_NOTIFY_CONSOLE"))) ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cAn update was found! (v" + updateChecker.getLatestVersion() + ") Download here: " + updateChecker.getDownloadUrl());
            }
        } catch (IOException exception) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cAn error occurred while checking for updates!");
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setDefaultCooldown() {
        for(Player all : Bukkit.getOnlinePlayers()) {
            if(all.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() != 4) all.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setOnlinePlayersCooldown() {
        for(Player all : Bukkit.getOnlinePlayers()) {
            String world = all.getLocation().getWorld().getName();
            if(WorldManager.isWorldDisabled(world)) return;

            all.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ObjectTransformer.getInteger(cacheContainer.get(Integer.class, "ATTACK_SPEED_VALUE")));
        }
    }

    @SuppressWarnings("unused")
    private void bStats() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aLoad and activate bStats ...");

        Metrics metrics = new Metrics(this, 3440);

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§abStats was successfully loaded and activated!");
    }

    public static PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public static Data getData() {
        return data;
    }

    public static UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public static TubeTilsManager getTubeTilsManager() {
        return tubeTilsManager;
    }

    public static CacheContainer getCacheContainer() {
        return cacheContainer;
    }

    public static AntiCooldown getInstance() {
        return main;
    }
}
