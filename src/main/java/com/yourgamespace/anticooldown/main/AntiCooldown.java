package com.yourgamespace.anticooldown.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.yourgamespace.anticooldown.commands.CmdAntiCooldown;
import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.files.PluginConfig;
import com.yourgamespace.anticooldown.listener.*;
import com.yourgamespace.anticooldown.utils.*;
import de.tubeof.tubetils.api.cache.CacheContainer;
import de.tubeof.tubetils.api.updatechecker.UpdateChecker;
import de.tubeof.tubetils.api.updatechecker.enums.ApiMethode;
import de.tubeof.tubetilsmanager.TubeTilsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings("ALL")
public class AntiCooldown extends JavaPlugin {

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private static AntiCooldown main;
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

        compatibilityTest();
        registerListener();
        registerCommands();
        registerPlaceholders();

        setOnlinePlayersCooldown();
        bStats();

        long startTime = System.currentTimeMillis() - startTimestamp;
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe plugin was successfully activated in §e" + startTime + "ms§a!");
    }

    @Override
    public void onDisable() {
        if(!tubeTilsManager.wasSuccessful()) return;

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe Plugin will be deactivated ...");

        setDefaultCooldown();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe plugin was successfully deactivated!");
    }

    private void initialisation() {
        main = this;

        tubeTilsManager = new TubeTilsManager("§7[§3AntiCooldownLogger§7] ", this, 71, true);
        cacheContainer = new CacheContainer("AntiCooldown");
        cacheContainer.registerCacheType(String.class);
        cacheContainer.registerCacheType(Boolean.class);
        cacheContainer.registerCacheType(Integer.class);
        cacheContainer.add(String.class, "STARTUP_PREFIX", "§7[§3AntiCooldownLogger§7] ");

        data = new Data();
        pluginConfig = new PluginConfig();
        versionHandler = new VersionHandler();

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

    private void compatibilityTest() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aRunning Compatibility-Check ...");

        /**
         * START
         * @see SweepAttack#onSweepAttackDamage(EntityDamageByEntityEvent) 
         */
        if(AntiCooldown.getVersionHandler().getVersionId() == -1) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§4WARNING: §cDisableSweepAttacks is not supported by §e" + Bukkit.getBukkitVersion() + "§c!");
        } else if (AntiCooldown.getVersionHandler().getVersionId() <= 7) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§4WARNING: §cDisableSweepAttacks is not supported by §e" + Bukkit.getBukkitVersion() + "§c!");
        }
        /**
         * END
         */

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aCompatibility-Check done!");
    }

    private void manageConfigs() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aLoading Config Files ...");

        pluginConfig.cfgConfig();
        pluginConfig.setCache();
        if(!ObjectTransformer.getInteger(cacheContainer.get(Integer.class, "CONFIG_VERSION")).equals(data.getCurrentConfigVersion())) pluginConfig.configUpdateMessage();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aConfig Files was successfully loaded!");
    }

    private void registerListener() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aListeners will be registered ...");

        // Bukkit Events
        pluginManager.registerEvents(new Join(), this);
        pluginManager.registerEvents(new Quit(), this);
        pluginManager.registerEvents(new SwitchWorld(), this);
        pluginManager.registerEvents(new SweepAttack(), this);
        pluginManager.registerEvents(new ItemRestriction(), this);
        pluginManager.registerEvents(new CustomItemDamage(), this);
        pluginManager.registerEvents(new EnderpearlCooldown(), this);
        pluginManager.registerEvents(new PlayerCollision(), this);

        // Packet Handler
        if(data.isProtocolLibInstalled()) {
            new SweepAttack.PacketHandler();
            new CombatSounds.PacketHandler();
        } else {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§4WARNING: §cDisableSweepAttackParticle is disabled: §cProtocolLib is missing!");
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§4WARNING: §cDisableNewCombatSounds is disabled: §cProtocolLib is missing!");
        }

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aListeners have been successfully registered!");
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
            updateChecker = new UpdateChecker("AntiCooldown-UpdateChecker", 51321, this, ApiMethode.YOURGAMESPACE, false, true);

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

    private void setDefaultCooldown() {
        CooldownHandler cooldownHandler = new CooldownHandler();

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(cooldownHandler.isCooldownDisabled(player)) cooldownHandler.enableCooldown(player);
        }
    }

    private void setOnlinePlayersCooldown() {
        CooldownHandler cooldownHandler = new CooldownHandler();

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String world = onlinePlayer.getLocation().getWorld().getName();

            // Check Bypass and Permissions
            boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && onlinePlayer.hasPermission("anticooldown.bypass");
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && onlinePlayer.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

            // If not permitted: Return;
            if(!isPermitted) return;

            if(WorldManager.isWorldDisabled(world)) {
                // If disabled and is bypassed, disable cooldown;
                // If disabled and is not bypassed, do nothing;
                if(isBypassed) cooldownHandler.disableCooldown(onlinePlayer);
                else cooldownHandler.enableCooldown(onlinePlayer);
            } else {
                cooldownHandler.disableCooldown(onlinePlayer);
            }
        }
    }

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

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public static CacheContainer getCacheContainer() {
        return cacheContainer;
    }

    public static VersionHandler getVersionHandler() {
        return versionHandler;
    }

    public static AntiCooldown getInstance() {
        return main;
    }
}
