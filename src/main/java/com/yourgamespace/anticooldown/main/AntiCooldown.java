package com.yourgamespace.anticooldown.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.yourgamespace.anticooldown.commands.CmdAntiCooldown;
import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.files.PluginConfig;
import com.yourgamespace.anticooldown.modules.*;
import com.yourgamespace.anticooldown.utils.*;
import de.tubeof.tubetils.api.cache.CacheContainer;
import de.tubeof.tubetils.api.updatechecker.UpdateChecker;
import de.tubeof.tubetils.api.updatechecker.enums.ApiMethode;
import de.tubeof.tubetilsmanager.TubeTilsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings({"ConstantConditions", "unused"})
public class AntiCooldown extends JavaPlugin {

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private static AntiCooldown main;
    private static LoggingHandler loggingHandler;
    private static ModuleHandler moduleHandler;
    private static VersionHandler versionHandler;
    private static TubeTilsManager tubeTilsManager;
    private static ProtocolManager protocolManager;
    private static CacheContainer cacheContainer;
    private static UpdateChecker updateChecker;
    private static Data data;
    private static PluginConfig pluginConfig;

    @Override
    public void onEnable() {
        long startTimestamp = System.currentTimeMillis();

        initialisation();
        if(!tubeTilsManager.wasSuccessful()) return;

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe Plugin will be activated ...");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "==================================================");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "JOIN OUR DISCORD: §ehttps://discord.gg/73ZDfbx");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "==================================================");

        manageConfigs();
        checkUpdate();

        registerModules();
        registerCommands();
        registerPlaceholders();

        bStats();

        long startTime = System.currentTimeMillis() - startTimestamp;
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe plugin was successfully activated in §e" + startTime + "ms§a!");

        // Code to run after plugin was enabled
        new CooldownHandler().setOnlinePlayersCooldown();
    }

    @Override
    public void onDisable() {
        if(!tubeTilsManager.wasSuccessful()) return;

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe Plugin will be deactivated ...");

        new CooldownHandler().setDefaultCooldown();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe plugin was successfully deactivated!");
    }

    private void initialisation() {
        main = this;

        tubeTilsManager = new TubeTilsManager("§7[§3AntiCooldownLogger§7] ", getInstance(), 71, true);
        cacheContainer = new CacheContainer("AntiCooldown");
        cacheContainer.registerCacheType(String.class);
        cacheContainer.registerCacheType(Boolean.class);
        cacheContainer.registerCacheType(Integer.class);
        cacheContainer.add(String.class, "STARTUP_PREFIX", "§7[§3AntiCooldownLogger§7] ");

        loggingHandler = new LoggingHandler();
        data = new Data();
        pluginConfig = new PluginConfig();
        versionHandler = new VersionHandler();
        moduleHandler = new ModuleHandler();

        //ProtocolLib
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aProtocolLib is installed! Support for ProtocolLib enabled!");
            data.setProtocolLib(true);

            protocolManager = ProtocolLibrary.getProtocolManager();
        } else {
            data.setProtocolLib(false);
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cProtocolLib is NOT installed! Support for ProtocolLib disabled!");
        }

        //PlaceholderAPI
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aPlaceholderAPI is installed! Support for PlaceholderAPI enabled!");
            data.setPlaceholderApi(true);
        } else {
            data.setPlaceholderApi(false);
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cPlaceholderAPI is NOT installed! Support for PlaceholderAPI disabled!");
        }
    }

    private void manageConfigs() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aLoading Config Files ...");

        pluginConfig.cfgConfig();
        pluginConfig.setCache();
        if(!ObjectTransformer.getInteger(cacheContainer.get(Integer.class, "CONFIG_VERSION")).equals(data.getCurrentConfigVersion())) pluginConfig.configUpdateMessage();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aConfig Files was successfully loaded!");
    }

    private void registerModules() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aModules will be registered ...");

        moduleHandler.registerModule(new UpdateNotifyOnJoin(false, true));
        moduleHandler.registerModule(new PvPCooldown(false, true));
        moduleHandler.registerModule(new SweepAttackDamage(false, true));
        moduleHandler.registerModule(new SweepAttackParticle(true, false));
        moduleHandler.registerModule(new CombatSounds(true, false));
        moduleHandler.registerModule(new EnderpearlCooldown(false, true));
        moduleHandler.registerModule(new PlayerCollision(true, true));
        moduleHandler.registerModule(new CustomItemDamage(false, true));
        moduleHandler.registerModule(new ItemRestriction(false, true));

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aModules have been successfully registered!");
    }

    private void registerCommands() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aCommands will be registered ...");

        getCommand("anticooldown").setExecutor(new CmdAntiCooldown());

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aCommands have been successfully registered!");
    }

    private void registerPlaceholders() {
        if(data.isPlaceholderApiInstalled()) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aPlaceholders for PlacerholderAPI will be registered ...");

            new PlaceholderHandler().register();

            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aPlaceholders have been successfully registered!");
        }
    }

    private void checkUpdate() {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_UPDATE_CHECKER"))) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cCheck for updates disabled. The check will be skipped!");
            return;
        }

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aChecking for updates ...");
        try {
            updateChecker = new UpdateChecker("AntiCooldown-UpdateChecker", 51321, getInstance(), ApiMethode.YOURGAMESPACE, false, true);

            // Check errors
            if(!updateChecker.isOnline()) {
                ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cUpdate-Check failed: No connection to the internet could be established.");
                return;
            }
            if(updateChecker.isRateLimited()) {
                ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cUpdate-Check failed: Request got blocked by rate limit!");
                return;
            }
            if(!updateChecker.wasSuccessful()) {
                ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cUpdate-Check failed: An unknown error has occurred!");
            }

            // Final outdated check
            if(updateChecker.isOutdated()) {
                if(ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "UPDATE_NOTIFY_CONSOLE"))) ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cAn update was found! (v" + updateChecker.getLatestVersion() + ") Download here: " + updateChecker.getDownloadUrl());
            }
        } catch (IOException exception) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cAn error occurred while checking for updates!");
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cPlease check the status page (https://yourgamespace.statuspage.io/) or contact our support (https://yourgamespace.com/support/).");
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void bStats() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aLoad and activate bStats ...");

        Metrics metrics = new Metrics(getInstance(), 3440);

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§abStats was successfully loaded and activated!");
    }
    
    public static AntiCooldown getInstance() {
        return main;
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

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public static CacheContainer getCacheContainer() {
        return cacheContainer;
    }

    public static VersionHandler getVersionHandler() {
        return versionHandler;
    }

    public static LoggingHandler getLoggingHandler() {
        return loggingHandler;
    }
}
