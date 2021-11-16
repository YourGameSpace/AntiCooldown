package com.yourgamespace.anticooldown.files;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ItemDamageManager;
import com.yourgamespace.anticooldown.utils.ItemRestrictionManager;
import com.yourgamespace.anticooldown.utils.LoggingHandler;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PluginConfig {

    private final LoggingHandler loggingHandler = AntiCooldown.getLoggingHandler();
    private final Data data = AntiCooldown.getData();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final File configFile = new File("plugins/AntiCooldown", "Config.yml");
    private FileConfiguration config;

    public PluginConfig() {
    }

    public void initConfigFile() {
        config = YamlConfiguration.loadConfiguration(configFile);

        // Pre-load config version for upgrade check
        cacheContainer.add(Integer.class, "CONFIG_VERSION", config.getInt("ConfigVersion"));
    }

    public void setupConfig() {
        if (!configFile.exists()) AntiCooldown.getInstance().saveResource(configFile.getName(), false);
    }

    public void upgradeConfig() {
        if (!ObjectTransformer.getInteger(cacheContainer.get(Integer.class, "CONFIG_VERSION")).equals(data.getCurrentConfigVersion())) {
            loggingHandler.info("§aUpgrading Config.yml to version §e" + data.getCurrentConfigVersion() + " §a...");

            // Create backup file object and delete if already exists
            File backup = new File(configFile.getParent(), configFile.getName() + "-backup.yml");
            if (backup.exists()) backup.delete();

            // Rename outdated config, create new config and init new config
            configFile.renameTo(backup);
            AntiCooldown.getInstance().saveResource(configFile.getName(), false);
            initConfigFile();

            loggingHandler.info("§aConfig.yml successfully upgraded! A backup file was created: §e" + configFile.getPath() + "-backup.yml");
        }
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException exception) {
            loggingHandler.warn("§cAn error occurred while trying to save config changes:");
            exception.printStackTrace();
        }
    }

    public void loadConfig() {
        loggingHandler.info("§aLoad configurations from config file §e" + configFile.getName() + " §a...");

        //Messages
        cacheContainer.add(String.class, "PREFIX", config.getString("Messages.Prefix"));
        cacheContainer.add(String.class, "ACTIONBAR_PREFIX", config.getString("Messages.ActionBarPrefix"));
        cacheContainer.add(String.class, "SWITCH_WORLD_BYPASSED", config.getString("Messages.SwitchWorld.Bypassed"));
        cacheContainer.add(String.class, "SWITCH_WORLD_ENABLED", config.getString("Messages.SwitchWorld.Enabled"));
        cacheContainer.add(String.class, "SWITCH_WORLD_DISABLED", config.getString("Messages.SwitchWorld.Disabled"));
        cacheContainer.add(String.class, "LOGIN_BYPASSED", config.getString("Messages.Login.Bypassed"));
        cacheContainer.add(String.class, "LOGIN_ENABLED", config.getString("Messages.Login.Enabled"));
        cacheContainer.add(String.class, "LOGIN_DISABLED", config.getString("Messages.Login.Disabled"));
        cacheContainer.add(String.class, "ITEM_RESTRICTION_ACTIONBAR_MESSAGE_ENABLED", config.getString("Messages.ItemRestriction.ActionBarMessage.Enabled"));
        cacheContainer.add(String.class, "ITEM_RESTRICTION_ACTIONBAR_MESSAGE_DISABLED", config.getString("Messages.ItemRestriction.ActionBarMessage.Disabled"));
        cacheContainer.add(String.class, "CUSTOM_ITEM_DAMAGE_ACTIONBAR_MESSAGE", config.getString("Messages.CustomItemDamage.ActionBarMessage"));
        cacheContainer.add(String.class, "SETTING_ADD_DISABLED_WORLD", config.getString("Messages.Setting.AddDisabledWorld"));
        cacheContainer.add(String.class, "SETTING_REMOVE_DISABLED_WORLD", config.getString("Messages.Setting.RemoveDisabledWorld"));
        cacheContainer.add(String.class, "ERROR_WORLD_ALRADY_LISTED", config.getString("Messages.Error.WorldAlreadyDisabled"));
        cacheContainer.add(String.class, "ERROR_WORLD_NOT_LISTED", config.getString("Messages.Error.WorldAlreadyEnabled"));
        cacheContainer.add(String.class, "ERROR_PLAYER_NOT_ONLINE", config.getString("Messages.Error.PlayerNotOnline"));
        cacheContainer.add(String.class, "ERROR_NO_PERMISSIONS", config.getString("Messages.Error.NoPerms"));

        //Placeholder
        cacheContainer.add(String.class, "PLACEHOLDER_WORLD_COOLDOWN_ENABLED", config.getString("Placeholder.World.CooldownEnabled"));
        cacheContainer.add(String.class, "PLACEHOLDER_WORLD_COOLDOWN_DISABLED", config.getString("Placeholder.World.CooldownDisabled"));
        cacheContainer.add(String.class, "PLACEHOLDER_PLAYER_COOLDOWN_ENABLED", config.getString("Placeholder.Player.CooldownEnabled"));
        cacheContainer.add(String.class, "PLACEHOLDER_PLAYER_COOLDOWN_DISABLED", config.getString("Placeholder.Player.CooldownDisabled"));

        //Settings
        //Settings: Permissions
        cacheContainer.add(Boolean.class, "USE_PERMISSIONS", config.getBoolean("Settings.Permissions.UsePermissions"));
        cacheContainer.add(Boolean.class, "USE_BYPASS_PERMISSION", config.getBoolean("Settings.Permissions.UseBypassPermission"));
        //Settings: Messages
        cacheContainer.add(Boolean.class, "USE_LOGIN_MESSAGES", config.getBoolean("Settings.Messages.UseLoginMessage"));
        cacheContainer.add(Boolean.class, "USE_SWITCH_WORLD_MESSAGES", config.getBoolean("Settings.Messages.UseSwitchWorldMessage"));
        //Settings: Values
        cacheContainer.add(Integer.class, "ATTACK_SPEED_VALUE", config.getInt("Settings.Values.AttackSpeed"));
        //Settings: Features
        cacheContainer.add(Boolean.class, "DISABLE_SWEEP_ATTACK", config.getBoolean("Settings.Features.DisableSweepAttacks"));
        cacheContainer.add(Boolean.class, "DISABLE_NEW_COMBAT_SOUNDS", config.getBoolean("Settings.Features.DisableNewCombatSounds"));
        cacheContainer.add(Boolean.class, "DISABLE_PLAYER_COLLISION", config.getBoolean("Settings.Features.DisablePlayerCollision"));
        cacheContainer.add(Boolean.class, "DISABLE_ENDERPEARL_COOLDOWN", config.getBoolean("Settings.Features.DisableEnderpearlCooldown"));
        cacheContainer.add(Boolean.class, "ENABLE_CUSTOM_ITEM_DAMAGE", config.getBoolean("Settings.Features.CustomItemDamage.EnableCustomItemDamage"));
        cacheContainer.add(Boolean.class, "ENABLE_CUSTOM_ITEM_DAMAGE_ACTIONBAR", config.getBoolean("Settings.Features.CustomItemDamage.SendActionBar"));
        cacheContainer.add(Boolean.class, "ITEM_RESTRICTION", config.getBoolean("Settings.Features.ItemRestriction.EnableItemRestriction"));
        cacheContainer.add(Boolean.class, "ENABLE_ITEM_RESTRICTION_ACTIONBAR", config.getBoolean("Settings.Features.ItemRestriction.SendActionBar"));
        cacheContainer.add(Boolean.class, "ITEM_RESTRICTION_AS_WHITELIST", config.getBoolean("Settings.Features.ItemRestriction.UseAsWhitelist"));
        //Settings: UpdateChecker
        cacheContainer.add(Boolean.class, "USE_UPDATE_CHECKER", config.getBoolean("Settings.Updates.UseUpdateChecker"));
        cacheContainer.add(Boolean.class, "UPDATE_NOTIFY_CONSOLE", config.getBoolean("Settings.Updates.ConsoleNotify"));
        cacheContainer.add(Boolean.class, "UPDATE_NOTIFY_INGAME", config.getBoolean("Settings.Updates.IngameNotify"));

        //Config Version
        cacheContainer.add(Integer.class, "CONFIG_VERSION", config.getInt("ConfigVersion"));

        //Values: RestrictedItems
        for (String restrictedItems : config.getStringList("Settings.Values.RestrictedItems")) {
            ItemRestrictionManager.addCache(restrictedItems);
        }

        //Values: DisabledWorlds
        for (String disabledWorld : config.getStringList("Settings.Values.DisabledWorlds")) {
            WorldManager.addCache(disabledWorld);
            loggingHandler.info("§aWorld §e" + disabledWorld + " §adisabled!");
        }

        //Values: CustomItemDamage
        for (String customItemDamage : config.getStringList("Settings.Values.CustomItemDamage")) {
            String[] itemParams = customItemDamage.split(":");

            ItemDamageManager.addCache(itemParams[0], itemParams[1]);
        }

        loggingHandler.info("§aConfigurations from config file §e" + configFile.getName() + " §asuccessfully loaded!");
    }

    public void setDisabledWorld(String world, boolean bol) {
        List<String> disabledWorlds = config.getStringList("Settings.Values.DisabledWorlds");

        if (bol) {
            disabledWorlds.add(world);
        } else {
            disabledWorlds.remove(world);
        }

        config.set("Settings.Values.DisabledWorlds", disabledWorlds);
        saveConfig();
    }
}
