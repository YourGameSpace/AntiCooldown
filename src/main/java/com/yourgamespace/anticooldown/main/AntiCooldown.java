package com.yourgamespace.anticooldown.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.yourgamespace.anticooldown.commands.CmdAntiCooldown;
import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.files.PluginConfig;
import com.yourgamespace.anticooldown.modules.CombatSounds;
import com.yourgamespace.anticooldown.modules.CustomItemDamage;
import com.yourgamespace.anticooldown.modules.EnderpearlCooldown;
import com.yourgamespace.anticooldown.modules.ItemRestriction;
import com.yourgamespace.anticooldown.modules.PlayerCollision;
import com.yourgamespace.anticooldown.modules.PvPCooldown;
import com.yourgamespace.anticooldown.modules.SweepAttackDamage;
import com.yourgamespace.anticooldown.modules.SweepAttackParticle;
import com.yourgamespace.anticooldown.modules.UpdateNotifyOnJoin;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.LoggingHandler;
import com.yourgamespace.anticooldown.utils.ModuleHandler;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.PlaceholderHandler;
import com.yourgamespace.anticooldown.utils.VersionHandler;
import de.tubeof.tubetils.api.cache.CacheContainer;
import de.tubeof.tubetils.api.updatechecker.UpdateChecker;
import de.tubeof.tubetils.api.updatechecker.enums.ApiMethode;
import de.tubeof.tubetilsmanager.TubeTilsManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings({"ConstantConditions", "unused"})
public class AntiCooldown extends JavaPlugin {

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
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

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

    public static ModuleHandler getModuleHandler() {
        return moduleHandler;
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

    @Override
    public void onEnable() {
        long startTimestamp = System.currentTimeMillis();

        initialisation();
        if (!tubeTilsManager.wasSuccessful()) return;

        loggingHandler.info("§aThe Plugin will be activated ...");
        loggingHandler.info("==================================================");
        loggingHandler.info("JOIN OUR DISCORD: §ehttps://discord.gg/73ZDfbx");
        loggingHandler.info("==================================================");

        manageConfigs();
        checkUpdate();

        registerModules();
        registerCommands();
        registerPlaceholders();

        bStats();

        long startTime = System.currentTimeMillis() - startTimestamp;
        loggingHandler.info("§aThe plugin was successfully activated in §e" + startTime + "ms§a!");

        // Code to run after plugin was enabled
        new CooldownHandler().setOnlinePlayersCooldown();
    }

    @Override
    public void onDisable() {
        if (!tubeTilsManager.wasSuccessful()) return;

        loggingHandler.info("§aThe Plugin will be deactivated ...");

        new CooldownHandler().setDefaultCooldown();
        moduleHandler.unregisterAllModules();

        loggingHandler.info("§aThe plugin was successfully deactivated!");
    }

    private void initialisation() {
        main = this;

        tubeTilsManager = new TubeTilsManager("[AntiCooldownLogger] ", getInstance(), 71, true);
        cacheContainer = new CacheContainer("AntiCooldown");
        cacheContainer.registerCacheType(String.class);
        cacheContainer.registerCacheType(Boolean.class);
        cacheContainer.registerCacheType(Integer.class);

        loggingHandler = new LoggingHandler();
        data = new Data();
        pluginConfig = new PluginConfig();
        versionHandler = new VersionHandler();
        moduleHandler = new ModuleHandler();

        new PluginConfig().setupConfig();

        //ProtocolLib
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            loggingHandler.info("§aProtocolLib is installed! Support for ProtocolLib enabled!");
            data.setProtocolLib(true);

            protocolManager = ProtocolLibrary.getProtocolManager();
        } else {
            data.setProtocolLib(false);
            loggingHandler.info("§cProtocolLib is NOT installed! Support for ProtocolLib disabled!");
        }

        //PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            loggingHandler.info("§aPlaceholderAPI is installed! Support for PlaceholderAPI enabled!");
            data.setPlaceholderApi(true);
        } else {
            data.setPlaceholderApi(false);
            loggingHandler.info("§cPlaceholderAPI is NOT installed! Support for PlaceholderAPI disabled!");
        }
    }

    private void manageConfigs() {
        loggingHandler.info("§aLoading config files ...");

        pluginConfig.setupConfig();
        pluginConfig.initConfigFile();
        pluginConfig.upgradeConfig();
        pluginConfig.loadConfig();

        loggingHandler.info("§aConfig files was successfully loaded!");
    }

    private void registerModules() {
        loggingHandler.info("§aModules will be registered ...");

        moduleHandler.registerModule(new UpdateNotifyOnJoin(false, true));
        moduleHandler.registerModule(new PvPCooldown(false, true));
        moduleHandler.registerModule(new SweepAttackDamage(false, true));
        moduleHandler.registerModule(new SweepAttackParticle(true, false));
        moduleHandler.registerModule(new CombatSounds(true, false));
        moduleHandler.registerModule(new EnderpearlCooldown(false, true));
        moduleHandler.registerModule(new PlayerCollision(true, true));
        moduleHandler.registerModule(new CustomItemDamage(false, true));
        moduleHandler.registerModule(new ItemRestriction(false, true));

        loggingHandler.info("§aModules have been successfully registered!");
    }

    private void registerCommands() {
        loggingHandler.info("§aCommands will be registered ...");

        getCommand("anticooldown").setExecutor(new CmdAntiCooldown());

        loggingHandler.info("§aCommands have been successfully registered!");

        loggingHandler.info("");
    }

    private void registerPlaceholders() {
        if (data.isPlaceholderApiInstalled()) {
            loggingHandler.info("§aPlaceholders for PlacerholderAPI will be registered ...");

            new PlaceholderHandler().register();

            loggingHandler.info("§aPlaceholders have been successfully registered!");
        }
    }

    private void checkUpdate() {
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_UPDATE_CHECKER"))) {
            loggingHandler.info("§cCheck for updates disabled. The check will be skipped!");
            return;
        }

        loggingHandler.info("§aChecking for updates ...");
        try {
            updateChecker = new UpdateChecker("AntiCooldown-UpdateChecker", 51321, getInstance(), ApiMethode.YOURGAMESPACE, false, true);

            // Check errors
            if (!updateChecker.isOnline()) {
                loggingHandler.info("§cUpdate-Check failed: No connection to the internet could be established.");
                return;
            }
            if (updateChecker.isRateLimited()) {
                loggingHandler.info("§cUpdate-Check failed: Request got blocked by rate limit!");
                return;
            }
            if (!updateChecker.wasSuccessful()) {
                loggingHandler.info("§cUpdate-Check failed: An unknown error has occurred!");
            }

            // Final outdated check
            if (updateChecker.isOutdated()) {
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "UPDATE_NOTIFY_CONSOLE")))
                    loggingHandler.info("§cAn update was found! (v" + updateChecker.getLatestVersion() + ") Download here: " + updateChecker.getDownloadUrl());
            }
        } catch (IOException exception) {
            loggingHandler.info("§cAn error occurred while checking for updates!");
            loggingHandler.info("§cPlease check the status page (https://yourgamespace.statuspage.io/) or contact our support (https://yourgamespace.com/support/).");
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void bStats() {
        loggingHandler.info("§aLoad and activate bStats ...");

        Metrics metrics = new Metrics(getInstance(), 3440);

        loggingHandler.info("§abStats was successfully loaded and activated!");
    }
}
