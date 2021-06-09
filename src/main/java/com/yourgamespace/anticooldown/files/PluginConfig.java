package com.yourgamespace.anticooldown.files;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class PluginConfig {

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final Data data = AntiCooldown.getData();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    private final WorldManager worldManager = new WorldManager();

    public PluginConfig() {}

    private File file = new File("plugins/AntiCooldown", "Config.yml");
    private FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public void configUpdateMessage() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§e######################################################################");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cA new config is included in the update!");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cPlease delete the old config so that the changes will be applied.");
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§e######################################################################");
    }

    private void saveCFG() {
        try {
            cfg.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cfgConfig() {
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
                ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§cConfig.yml could not be created!");
            }
        }
    }

    public void setCache() {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aConfig values are loaded into the cache ...");

        //Messages
        cacheContainer.add(String.class, "PREFIX", cfg.getString("Messages.Prefix"));
        cacheContainer.add(String.class, "SWITCH_WORLD_ENABLED", cfg.getString("Messages.Switch.WorldEnabled"));
        cacheContainer.add(String.class, "SWITCH_WORLD_DISABLED", cfg.getString("Messages.Switch.WorldDisabled"));
        cacheContainer.add(String.class, "LOGIN_ENABLED", cfg.getString("Messages.Login.Enabled"));
        cacheContainer.add(String.class, "LOGIN_DISABLED", cfg.getString("Messages.Login.Disabled"));
        cacheContainer.add(String.class, "SETTING_ADD_DISABLED_WORLD", cfg.getString("Messages.Setting.AddDisabledWorld"));
        cacheContainer.add(String.class, "SETTING_REMOVE_DISABLED_WORLD", cfg.getString("Messages.Setting.RemoveDisabledWorld"));
        cacheContainer.add(String.class, "ERROR_WORLD_ALRADY_LISTED", cfg.getString("Messages.Error.WorldAlreadyDisabled"));
        cacheContainer.add(String.class, "ERROR_WORLD_NOT_LISTED", cfg.getString("Messages.Error.WorldAlreadyEnabled"));
        cacheContainer.add(String.class, "ERROR_PLAYER_NOT_ONLINE", cfg.getString("Messages.Error.PlayerNotOnline"));
        cacheContainer.add(String.class, "ERROR_NO_PERMISSIONS", cfg.getString("Messages.Error.NoPerms"));

        //Settings
        cacheContainer.add(Boolean.class, "USE_LOGIN_MESSAGES", cfg.getBoolean("Settings.Messages.UseLoginMessage"));
        cacheContainer.add(Boolean.class, "USE_SWITCH_WORLD_MESSAGES", cfg.getBoolean("Settings.Messages.UseSwitchWorldMessage"));
        cacheContainer.add(Boolean.class, "USE_UPDATE_CHECKER", cfg.getBoolean("Settings.Updates.UseUpdateChecker"));
        cacheContainer.add(Boolean.class, "UPDATE_NOTIFY_CONSOLE", cfg.getBoolean("Settings.Updates.ConsoleNotify"));
        cacheContainer.add(Boolean.class, "UPDATE_NOTIFY_INGAME", cfg.getBoolean("Settings.Updates.IngameNotify"));
        cacheContainer.add(Boolean.class, "DISABLE_SWEEP_ATTACK", cfg.getBoolean("Settings.Features.DisableSweepAttacks"));
        cacheContainer.add(Integer.class, "ATTACK_SPEED_VALUE", cfg.getInt("Settings.Values.AttackSpeed"));

        data.setConfigVersion(cfg.getInt("ConfigVersion"));

        for (String disabledWorld : cfg.getStringList("Settings.DisabledWorlds")) {
            worldManager.disableWorld(disabledWorld);
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aWorld §e" + disabledWorld + " §adisabled!");
        }

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aConfig values were successfully cached!");
    }

    public void setDisabledWorld(String world, boolean bol) {
        List<String> disabledWorlds = cfg.getStringList("Settings.DisabledWorlds");

        if(bol) {
            disabledWorlds.add(world);
        }else {
            disabledWorlds.remove(world);
        }

        cfg.set("Settings.DisabledWorlds", disabledWorlds);
        saveCFG();
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
}
