package com.yourgamespace.anticooldown.main;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.data.Messages;
import com.yourgamespace.anticooldown.enums.MessageType;
import com.yourgamespace.anticooldown.enums.SettingsType;
import com.yourgamespace.anticooldown.files.Config;
import com.yourgamespace.anticooldown.listener.Join;
import com.yourgamespace.anticooldown.listener.Quit;
import com.yourgamespace.anticooldown.listener.SweepAttack;
import com.yourgamespace.anticooldown.listener.SwitchWorld;
import com.yourgamespace.anticooldown.utils.Metrics;
import com.yourgamespace.anticooldown.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiCooldown extends JavaPlugin {


    private final static Messages messages = new Messages();
    private final static Data data = new Data();
    private static AntiCooldown main;

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aThe Plugin will be activated ...");

        main = this;

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "==================================================");
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "JOIN MY DISCORD NOW: §ehttps://discord.gg/73ZDfbx");
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "==================================================");

        manageConfigs();
        registerListener();
        registerCommands();
        checkUpdate();

        setOnlinePlayersCooldown();
        bStats();

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aThe plugin was successfully activated!");
    }

    @Override
    public void onDisable() {
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aThe Plugin will be deactivated ...");

        setDefaultCooldown();

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aThe plugin was successfully deactivated!");
    }

    private void manageConfigs() {
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aLoading Config Files ...");

        Config.cfgConfig();
        Config.setChache();
        if(data.getConfigVersion() != 7) Config.configUpdateMessage();

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aConfig Files was successfully loaded!");
    }

    private void registerListener() {
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aListeners will be registered ...");

        pluginManager.registerEvents(new Join(), this);
        pluginManager.registerEvents(new Quit(), this);
        pluginManager.registerEvents(new SwitchWorld(), this);
        pluginManager.registerEvents(new SweepAttack(), this);
        //pluginManager.registerEvents(new ItemRestriction(), this);

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aListeners have been successfully registered!");
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aCommands will be registered ...");

        getCommand("anticooldown").setExecutor(new com.yourgamespace.anticooldown.commands.AntiCooldown());

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aCommands have been successfully registered!");
    }

    private void checkUpdate() {
        if(!data.getBooleanSettings(SettingsType.USE_UPDATE_CHECKER)) {
            ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§cCheck for updates disabled. The check will be skipped!");
            return;
        }

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aChecking for updates ...");
        UpdateChecker updateChecker = new UpdateChecker(51321, this);

        if(updateChecker.getUpdateCheckResult() == UpdateChecker.UpdateCheckResult.UP_TO_DATE) {
            data.setUpdateAvailable(false);
            ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aNo update available!");
            return;
        }
        if(updateChecker.getUpdateCheckResult() == UpdateChecker.UpdateCheckResult.OUT_DATED) {
            data.setUpdateAvailable(true);
            if(data.getBooleanSettings(SettingsType.UPDATE_NOTIFY_CONSOLE)) ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§cAn update was found! Download here: " + updateChecker.getResourceURL());
            return;
        }
        if(updateChecker.getUpdateCheckResult() == UpdateChecker.UpdateCheckResult.UNRELEASED) {
            ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aThis version will be released in the future!");
            return;
        }
        if(updateChecker.getUpdateCheckResult() == UpdateChecker.UpdateCheckResult.NO_RESULT) {
            ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§cAn error occurred in the dpdate checker. Possibly the API is currently offline.");
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
            if(data.isWorldDisabled(world)) return;

            all.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(data.getIntegerSettings(SettingsType.ATTACK_SPEED_VALUE));
        }
    }

    @SuppressWarnings("unused")
    private void bStats() {
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aLoad and activate bStats ...");

        Metrics metrics = new Metrics(this, 3440);

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§abStats was successfully loaded and activated!");
    }

    public static Messages getMessages() {
        return messages;
    }

    public static Data getData() {
        return data;
    }

    public static AntiCooldown getInstance() {
        return main;
    }
}
