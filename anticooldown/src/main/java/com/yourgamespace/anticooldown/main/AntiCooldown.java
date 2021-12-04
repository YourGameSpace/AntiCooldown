package com.yourgamespace.anticooldown.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.yourgamespace.anticooldown.commands.CmdAntiCooldown;
import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.files.PluginConfig;
import com.yourgamespace.anticooldown.modules.AttackCooldown;
import com.yourgamespace.anticooldown.modules.CombatSounds;
import com.yourgamespace.anticooldown.modules.CustomItemDamage;
import com.yourgamespace.anticooldown.modules.EnderpearlCooldown;
import com.yourgamespace.anticooldown.modules.ItemRestriction;
import com.yourgamespace.anticooldown.modules.PlayerCollision;
import com.yourgamespace.anticooldown.modules.SweepAttackDamage;
import com.yourgamespace.anticooldown.modules.SweepAttackParticle;
import com.yourgamespace.anticooldown.modules.UpdateNotifyOnJoin;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.basics.AntiCooldownLogger;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.PlaceholderHandler;
import com.yourgamespace.anticooldown.utils.basics.VersionHandler;
import com.yourgamespace.anticooldown.utils.WorldManager;
import com.yourgamespace.anticooldown.utils.module.ModuleCommandHandler;
import com.yourgamespace.anticooldown.utils.module.ModuleDescription;
import com.yourgamespace.anticooldown.utils.module.ModuleHandler;
import com.yourgamespace.anticooldown.utils.module.ModulePlaceholderHandler;
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
    private static TubeTilsManager tubeTilsManager;
    private static CacheContainer cacheContainer;
    private static AntiCooldownLogger antiCooldownLogger;
    private static Data data;
    private static WorldManager worldManager;
    private static PluginConfig pluginConfig;
    private static VersionHandler versionHandler;
    private static ModuleHandler moduleHandler;
    private static ModuleCommandHandler moduleCommandHandler;
    private static ModulePlaceholderHandler modulePlaceholderHandler;
    private static ProtocolManager protocolManager;
    private static UpdateChecker updateChecker;


    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        long startTimestamp = System.currentTimeMillis();

        initialisation();
        if (!tubeTilsManager.wasSuccessful()) return;

        antiCooldownLogger.info("§aThe Plugin will be activated ...");
        antiCooldownLogger.info("==================================================");
        antiCooldownLogger.info("JOIN OUR DISCORD: §ehttps://discord.gg/73ZDfbx");
        antiCooldownLogger.info("==================================================");

        manageConfigs();
        checkUpdate();

        registerModules();
        registerCommands();
        registerPlaceholders();

        setupMetrics();

        long startTime = System.currentTimeMillis() - startTimestamp;
        antiCooldownLogger.info("§aThe plugin was successfully activated in §e" + startTime + "ms§a!");
    }

    @Override
    public void onDisable() {
        if (!tubeTilsManager.wasSuccessful()) return;

        antiCooldownLogger.info("§aThe Plugin will be deactivated ...");

        new CooldownHandler().setDefaultCooldown();
        moduleHandler.unregisterAllModules();

        antiCooldownLogger.info("§aThe plugin was successfully deactivated!");
    }

    private void initialisation() {
        main = this;

        tubeTilsManager = new TubeTilsManager("[AntiCooldownLogger] ", getInstance(), 71, true);
        cacheContainer = new CacheContainer("AntiCooldown");
        cacheContainer.registerCacheType(String.class);
        cacheContainer.registerCacheType(Boolean.class);
        cacheContainer.registerCacheType(Integer.class);

        antiCooldownLogger = new AntiCooldownLogger();
        data = new Data();
        worldManager = new WorldManager();
        pluginConfig = new PluginConfig();
        versionHandler = new VersionHandler();
        moduleHandler = new ModuleHandler();
        moduleCommandHandler = new ModuleCommandHandler();
        modulePlaceholderHandler = new ModulePlaceholderHandler();

        //ProtocolLib
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            antiCooldownLogger.info("§aProtocolLib is installed! Support for ProtocolLib enabled!");
            data.setProtocolLib(true);

            protocolManager = ProtocolLibrary.getProtocolManager();
        } else {
            data.setProtocolLib(false);
            antiCooldownLogger.info("§cProtocolLib is NOT installed! Support for ProtocolLib disabled!");
        }

        //PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            antiCooldownLogger.info("§aPlaceholderAPI is installed! Support for PlaceholderAPI enabled!");
            data.setPlaceholderApi(true);
        } else {
            data.setPlaceholderApi(false);
            antiCooldownLogger.info("§cPlaceholderAPI is NOT installed! Support for PlaceholderAPI disabled!");
        }
    }

    private void manageConfigs() {
        antiCooldownLogger.info("§aLoading config files ...");

        pluginConfig.setupConfig();
        pluginConfig.initConfigFile();
        pluginConfig.upgradeConfig();
        pluginConfig.loadConfig();

        antiCooldownLogger.info("§aConfig files was successfully loaded!");
    }

    private void registerModules() {
        antiCooldownLogger.info("§aModules will be registered ...");

        moduleHandler.registerModule(new UpdateNotifyOnJoin(false, true, new ModuleDescription("UpdateNotifyOnJoin", "1.0", "Internal Module", "YourGameSpace")));
        moduleHandler.registerModule(new AttackCooldown(false, true, new ModuleDescription("AttackCooldown", "1.0", "Internal Module", "YourGameSpace")));
        moduleHandler.registerModule(new SweepAttackDamage(false, true, new ModuleDescription("SweepAttackDamage", "1.0", "Internal Module", "YourGameSpace")));
        moduleHandler.registerModule(new SweepAttackParticle(true, false, new ModuleDescription("SweepAttackParticle", "1.0", "Internal Module", "YourGameSpace")));
        moduleHandler.registerModule(new CombatSounds(true, false, new ModuleDescription("CombatSounds", "1.0", "Internal Module", "YourGameSpace")));
        moduleHandler.registerModule(new EnderpearlCooldown(false, true, new ModuleDescription("EnderpearlCooldown", "1.0", "Internal Module", "YourGameSpace")));
        moduleHandler.registerModule(new PlayerCollision(true, true, new ModuleDescription("PlayerCollision", "1.0", "Internal Module", "YourGameSpace")));
        moduleHandler.registerModule(new CustomItemDamage(false, true, new ModuleDescription("CustomItemDamage", "1.0", "Internal Module", "YourGameSpace")));
        moduleHandler.registerModule(new ItemRestriction(false, true, new ModuleDescription("ItemRestriction", "1.0", "Internal Module", "YourGameSpace")));

        moduleHandler.enableModules();

        antiCooldownLogger.info("§aModules have been successfully registered!");
    }

    private void registerCommands() {
        antiCooldownLogger.info("§aCommands will be registered ...");

        getCommand("anticooldown").setExecutor(new CmdAntiCooldown());
        getCommand("anticooldown").setTabCompleter(new CmdAntiCooldown());

        antiCooldownLogger.info("§aCommands have been successfully registered!");
    }

    private void registerPlaceholders() {
        if (data.isPlaceholderApiInstalled()) {
            antiCooldownLogger.info("§aPlaceholders for PlacerholderAPI will be registered ...");

            new PlaceholderHandler().register();

            antiCooldownLogger.info("§aPlaceholders have been successfully registered!");
        }
    }

    private void checkUpdate() {
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_UPDATE_CHECKER"))) {
            antiCooldownLogger.info("§cCheck for updates disabled. The check will be skipped!");
            return;
        }

        antiCooldownLogger.info("§aChecking for updates ...");
        try {
            updateChecker = new UpdateChecker("AntiCooldown-UpdateChecker", 51321, getInstance(), ApiMethode.YOURGAMESPACE, false, true);

            // Check errors
            if (!updateChecker.isOnline()) {
                antiCooldownLogger.info("§cUpdate-Check failed: No connection to the internet could be established.");
                return;
            }
            if (updateChecker.isRateLimited()) {
                antiCooldownLogger.info("§cUpdate-Check failed: Request got blocked by rate limit!");
                return;
            }
            if (!updateChecker.wasSuccessful()) {
                antiCooldownLogger.info("§cUpdate-Check failed: An unknown error has occurred!");
            }

            // Final outdated check
            if (updateChecker.isOutdated()) {
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "UPDATE_NOTIFY_CONSOLE"))) {
                    antiCooldownLogger.info("§cAn update was found! (v" + updateChecker.getLatestVersion() + ") Download here: " + updateChecker.getDownloadUrl());
                }
            }
        } catch (IOException exception) {
            antiCooldownLogger.info("§cAn error occurred while checking for updates!");
            antiCooldownLogger.info("§cPlease check the status page (https://yourgamespace.statuspage.io/) or contact our support (https://yourgamespace.com/support/).");
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void setupMetrics() {
        antiCooldownLogger.info("§aEnabling metrics ...");

        Metrics metrics = new Metrics(getInstance(), 3440);

        antiCooldownLogger.info("§aMetrics was successfully enabled!");
    }

    public static AntiCooldown getInstance() {
        return main;
    }

    public static CacheContainer getCacheContainer() {
        return cacheContainer;
    }

    public static AntiCooldownLogger getAntiCooldownLogger() {
        return antiCooldownLogger;
    }

    public static Data getData() {
        return data;
    }

    public static WorldManager getWorldManager() {
        return worldManager;
    }

    public static PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public static VersionHandler getVersionHandler() {
        return versionHandler;
    }

    public static ModuleHandler getModuleHandler() {
        return moduleHandler;
    }

    public static ModuleCommandHandler getModuleCommandHandler() {
        return moduleCommandHandler;
    }

    public static ModulePlaceholderHandler getModulePlaceholderHandler() {
        return modulePlaceholderHandler;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public static UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}
