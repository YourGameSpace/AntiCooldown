package de.tubeof.ac.files;

import de.tubeof.ac.data.Messages;
import de.tubeof.ac.enums.MessageType;
import de.tubeof.ac.data.Data;
import de.tubeof.ac.enums.SettingsType;
import de.tubeof.ac.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {

    private static ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private static Messages messages = Main.getMessages();
    private static Data data = Main.getData();

    private static File file = new File("plugins/AntiCooldown", "Config.yml");
    private static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public static void configUpdateMessage() {
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§e######################################################################");
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§cA new config is included in the update!");
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§cPlease delete the old config so that the changes will be applied.");
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§e######################################################################");
    }

    private static void saveCFG() {
        try {
            cfg.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void cfgConfig() {
        cfg.options().copyDefaults(true);

        //Messages
        cfg.addDefault("Messages.Prefix", "§7[§c§3AntiCooldown§7] ");
        cfg.addDefault("Messages.Switch.WorldEnabled", "§ePvP Cooldown is §a§ldisabled §ein this world!");
        cfg.addDefault("Messages.Switch.WorldDisabled", "§ePvP Cooldown is §c§lnot disabled §ein this world!");
        cfg.addDefault("Messages.Login.Enabled", "§aHey, welcome to the server! §ePvP Cooldown is §a§ldisabled §ein this world!");
        cfg.addDefault("Messages.Login.Disabled", "§aHey, welcome to the server! §ePvP Cooldown is §c§lnot disabled §ein this world!");
        cfg.addDefault("Messages.Setting.AddDisabledWorld", "§aOK! In the world §e%world% §athe cooldown is now activated.");
        cfg.addDefault("Messages.Setting.RemoveDisabledWorld", "§aOK! In the world §e%world% §athe cooldown is now deactivated.");
        cfg.addDefault("Messages.Error.WorldAlreadyDisabled", "§cThis world is already §c§ldeactivated§c!");
        cfg.addDefault("Messages.Error.WorldAlreadyEnabled", "§cThis world is already §a§lactivated§c!");
        cfg.addDefault("Messages.Error.PlayerNotOnline", "§cThe player is not online!");
        cfg.addDefault("Messages.Error.NoPerms", "§cNo permissions!");

        //Settings
        cfg.addDefault("Settings.Messages.UseLoginMessage", true);
        cfg.addDefault("Settings.Messages.UseSwitchWorldMessage", true);
        cfg.addDefault("Settings.Values.AttackSpeed", 100);
        cfg.addDefault("Settings.Features.DisableSweepAttacks", true);
        cfg.addDefault("Settings.Updates.UseUpdateChecker", true);
        cfg.addDefault("Settings.Updates.ConsoleNotify", true);
        cfg.addDefault("Settings.Updates.IngameNotify", true);

        //Disabled Worlds
        List<String> disabledWorlds = cfg.getStringList("Settings.DisabledWorlds");
        disabledWorlds.add("YourWorldName");
        cfg.addDefault("Settings.DisabledWorlds", disabledWorlds);

        cfg.addDefault("ConfigVersion", 7);

        saveCFG();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception localExeption) {
                ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§cConfig.yml could not be created!");
            }
        }
    }

    public static void setChache() {
        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aConfig values are loaded into the cache ...");

        //Messages
        messages.setTextMessages(MessageType.PREFIX, cfg.getString("Messages.Prefix"));
        messages.setTextMessages(MessageType.SWITCH_WORLD_ENABLED, cfg.getString("Messages.Switch.WorldEnabled"));
        messages.setTextMessages(MessageType.SWITCH_WORLD_DISABLED, cfg.getString("Messages.Switch.WorldDisabled"));
        messages.setTextMessages(MessageType.LOGIN_ENABLED, cfg.getString("Messages.Login.Enabled"));
        messages.setTextMessages(MessageType.LOGIN_DISABLED, cfg.getString("Messages.Login.Disabled"));
        messages.setTextMessages(MessageType.SETTING_ADD_DISABLED_WORLD, cfg.getString("Messages.Setting.AddDisabledWorld"));
        messages.setTextMessages(MessageType.SETTING_REMOVE_DISABLED_WORLD, cfg.getString("Messages.Setting.RemoveDisabledWorld"));
        messages.setTextMessages(MessageType.ERROR_WORLD_ALRADY_LISTED, cfg.getString("Messages.Error.WorldAlreadyDisabled"));
        messages.setTextMessages(MessageType.ERROR_WORLD_NOT_LISTED, cfg.getString("Messages.Error.WorldAlreadyEnabled"));
        messages.setTextMessages(MessageType.ERROR_PLAYER_NOT_ONLINE, cfg.getString("Messages.Error.PlayerNotOnline"));
        messages.setTextMessages(MessageType.ERROR_NO_PERMISSIONS, cfg.getString("Messages.Error.NoPerms"));

        //Settings
        data.setBooleanSettings(SettingsType.USE_LOGIN_MESSAGES, cfg.getBoolean("Settings.Messages.UseLoginMessage"));
        data.setBooleanSettings(SettingsType.USE_SWITCH_WORLD_MESSAGES, cfg.getBoolean("Settings.Messages.UseSwitchWorldMessage"));
        data.setBooleanSettings(SettingsType.USE_UPDATE_CHECKER, cfg.getBoolean("Settings.Updates.UseUpdateChecker"));
        data.setBooleanSettings(SettingsType.UPDATE_NOTIFY_CONSOLE, cfg.getBoolean("Settings.Updates.ConsoleNotify"));
        data.setBooleanSettings(SettingsType.UPDATE_NOTIFY_INGAME, cfg.getBoolean("Settings.Updates.IngameNotify"));
        data.setBooleanSettings(SettingsType.DISABLE_SWEEP_ATTACK, cfg.getBoolean("Settings.Features.DisableSweepAttacks"));
        data.setIntegerSettings(SettingsType.ATTACK_SPEED_VALUE, cfg.getInt("Settings.Values.AttackSpeed"));

        data.setConfigVersion(cfg.getInt("ConfigVersion"));

        for (String disabledWorld : cfg.getStringList("Settings.DisabledWorlds")) {
            data.addDisableWorldToCache(disabledWorld);
            ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aWorld §e" + disabledWorld + " §adisabled!");
        }

        ccs.sendMessage(messages.getTextMessage(MessageType.STARTUP_PREFIX) + "§aConfig values were successfully cached!");
    }

    public static void setDisabledWorld(String world, boolean bol) {
        List<String> disabledWorlds = cfg.getStringList("Settings.DisabledWorlds");

        if(bol) {
            disabledWorlds.add(world);
        }else {
            disabledWorlds.remove(world);
        }

        cfg.set("Settings.DisabledWorlds", disabledWorlds);
        saveCFG();
    }
}
