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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings("ALL")
public class AntiCooldown extends JavaPlugin {

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private static AntiCooldown main;
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

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe Plugin will be activated ...");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "==================================================");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "JOIN OUR DISCORD: §ehttps://discord.gg/73ZDfbx");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "==================================================");

        manageConfigs();
        checkUpdate();

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
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe Plugin will be deactivated ...");

        setDefaultCooldown();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aThe plugin was successfully deactivated!");
    }

    private void initialisation() {
        main = this;

        tubeTilsManager = new TubeTilsManager("§7[§3AntiCooldownLogger§7] ", this, "SNAPSHOT-52", "1.0.3", true);
        cacheContainer = new CacheContainer("AntiCooldown");
        cacheContainer.registerCacheType(String.class);
        cacheContainer.registerCacheType(Boolean.class);
        cacheContainer.registerCacheType(Integer.class);
        cacheContainer.add(String.class, "STARTUP_PREFIX", "§7[§3AntiCooldownLogger§7] ");

        data = new Data();
        pluginConfig = new PluginConfig();

        //ProtocolLib
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aProtocolLib is installed! Support for ProtocolLib enabled!");
            data.setProtocollib(true);

            protocolManager = ProtocolLibrary.getProtocolManager();
        } else {
            data.setProtocollib(false);
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cProtocolLib is NOT installed! Support for ProtocolLib disabled!");
        }
    }

    private void manageConfigs() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aLoading Config Files ...");

        pluginConfig.cfgConfig();
        pluginConfig.setCache();
        if(ObjectTransformer.getInteger(cacheContainer.get(Integer.class, "CONFIG_VERSION")) != data.getCurrentConfigVersion()) pluginConfig.configUpdateMessage();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aConfig Files was successfully loaded!");
    }

    private void registerListener() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aListeners will be registered ...");

        // Bukkit Events
        pluginManager.registerEvents(new Join(), this);
        pluginManager.registerEvents(new Quit(), this);
        pluginManager.registerEvents(new SwitchWorld(), this);
        pluginManager.registerEvents(new SweepAttack(), this);
        //pluginManager.registerEvents(new ItemRestriction(), this);

        // Packet Handler
        new SweepAttack.PacketHandler();
        new CombatSounds.PacketHandler();

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aListeners have been successfully registered!");
    }

    private void registerCommands() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aCommands will be registered ...");

        getCommand("anticooldown").setExecutor(new CmdAntiCooldown());

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aCommands have been successfully registered!");
    }

    private void registerPlaceholders() {
        if(pluginManager.getPlugin("PlaceholderAPI") != null) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aPlaceholders for PlacerholderAPI will be registered ...");

            new PlaceholderHandler().register();
        } else {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§ePlaceholderAPI is not installed! Disabling placeholders ...");
        }
    }

    private void checkUpdate() {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_UPDATE_CHECKER"))) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cCheck for updates disabled. The check will be skipped!");
            return;
        }

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aChecking for updates ...");
        try {
            updateChecker = new UpdateChecker(51321, this, ApiMethode.YOURGAMESPACE, false, true);
            if(updateChecker.isOutdated()) {
                if(ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "UPDATE_NOTIFY_CONSOLE"))) ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cAn update was found! (v" + updateChecker.getLatestVersion() + ") Download here: " + updateChecker.getDownloadUrl());
            }
        } catch (IOException exception) {
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cAn error occurred while checking for updates!");
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
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS")) && onlinePlayer.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS"));

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

    public static AntiCooldown getInstance() {
        return main;
    }
}
