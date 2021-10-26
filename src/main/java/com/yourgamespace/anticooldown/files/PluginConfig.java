package com.yourgamespace.anticooldown.files;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ItemDamageManager;
import com.yourgamespace.anticooldown.utils.ItemRestrictionManager;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PluginConfig {

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final Data data = AntiCooldown.getData();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public PluginConfig() {}

    private final File file = new File("plugins/AntiCooldown", "Config.yml");
    private final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

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
        cfg.options().header("Looking for config documentation? https://hub.yourgamespace.com/docs/anticooldown/config/#configyml");


        //Messages
        cfg.addDefault("Messages.Prefix", "§7[§3AntiCooldown§7] ");
        cfg.addDefault("Messages.ActionBarPrefix", "§3AntiCooldown§7 »");
        cfg.addDefault("Messages.SwitchWorld.Bypassed", "§ePvP Cooldown is §c§lnot disabled §ein this world, but you have §2Bypass-Permissions§a! §aCooldown is disable for you.");
        cfg.addDefault("Messages.SwitchWorld.Enabled", "§ePvP Cooldown is §a§ldisabled §ein this world!");
        cfg.addDefault("Messages.SwitchWorld.Disabled", "§ePvP Cooldown is §c§lnot disabled §ein this world!");
        cfg.addDefault("Messages.Login.Bypassed", "§aHey, welcome to the server! §ePvP Cooldown is §c§lnot disabled §ein this world, but you have §2Bypass-Permissions§a! §aCooldown is disable for you.");
        cfg.addDefault("Messages.Login.Enabled", "§aHey, welcome to the server! §ePvP Cooldown is §a§ldisabled §ein this world!");
        cfg.addDefault("Messages.Login.Disabled", "§aHey, welcome to the server! §ePvP Cooldown is §c§lnot disabled §ein this world!");
        cfg.addDefault("Messages.CustomItemDamage.ActionBarMessage", "%actionbar_prefix% §aCustom-Damage applied: §7%finaldamage%§c❤ §7Damage");
        cfg.addDefault("Messages.ItemRestriction.ActionBarMessage.Enabled", "%actionbar_prefix% §aItem is no longer restricted! PvP Cooldown is §a§ldisabled §aagain");
        cfg.addDefault("Messages.ItemRestriction.ActionBarMessage.Disabled", "%actionbar_prefix% §cItem is restricted! PvP Cooldown is temporarily §c§lactivated");
        cfg.addDefault("Messages.Setting.AddDisabledWorld", "§aOK! In the world §e%world% §athe cooldown is now activated.");
        cfg.addDefault("Messages.Setting.RemoveDisabledWorld", "§aOK! In the world §e%world% §athe cooldown is now deactivated.");
        cfg.addDefault("Messages.Error.WorldAlreadyDisabled", "§cThis world is already §c§ldeactivated§c!");
        cfg.addDefault("Messages.Error.WorldAlreadyEnabled", "§cThis world is already §a§lactivated§c!");
        cfg.addDefault("Messages.Error.PlayerNotOnline", "§cThe player is not online!");
        cfg.addDefault("Messages.Error.NoPerms", "§cNo permissions!");

        //Placeholder
        cfg.addDefault("Placeholder.World.CooldownEnabled", "Enabled");
        cfg.addDefault("Placeholder.World.CooldownDisabled", "Disabled");
        cfg.addDefault("Placeholder.Player.CooldownEnabled", "Enabled");
        cfg.addDefault("Placeholder.Player.CooldownDisabled", "Disabled");

        // Config List Options
        //List: Restricted Items
        List<String> restrictedItems = cfg.getStringList("Settings.Values.RestrictedItems");
        restrictedItems.add("DIAMOND_AXE");
        restrictedItems.add("GOLDEN_AXE");
        restrictedItems.add("IRON_AXE");
        restrictedItems.add("STONE_AXE");
        restrictedItems.add("NETHERITE_AXE");
        restrictedItems.add("WOODEN_AXE");

        //List: Disabled Worlds
        List<String> disabledWorlds = cfg.getStringList("Settings.Values.DisabledWorlds");
        disabledWorlds.add("YourWorldName");

        //List: Item Damage Values
        List<String> customItemDamage = cfg.getStringList("Settings.Values.CustomItemDamage");
        customItemDamage.add("WOOD_AXE:3.0D");
        customItemDamage.add("WOODEN_AXE:3.0D");
        customItemDamage.add("GOLD_AXE:3.0D");
        customItemDamage.add("GOLDEN_AXE:3.0D");
        customItemDamage.add("STONE_AXE:4.0D");
        customItemDamage.add("IRON_AXE:5.0D");
        customItemDamage.add("DIAMOND_AXE:6.0D");
        customItemDamage.add("WOOD_PICKAXE:2.0D");
        customItemDamage.add("WOODEN_PICKAXE:2.0D");
        customItemDamage.add("GOLD_PICKAXE:2.0D");
        customItemDamage.add("GOLDEN_PICKAXE:2.0D");
        customItemDamage.add("STONE_PICKAXE:3.0D");
        customItemDamage.add("IRON_PICKAXE:4.0D");
        customItemDamage.add("DIAMOND_PICKAXE:5.0D");
        customItemDamage.add("WOODEN_SHOVEL:1.0D");
        customItemDamage.add("GOLDEN_SHOVEL:1.0D");
        customItemDamage.add("STONE_SHOVEL:2.0D");
        customItemDamage.add("IRON_SHOVEL:3.0D");
        customItemDamage.add("DIAMOND_SHOVEL:4.0D");

        //Settings
        //Settings: Permissions
        cfg.addDefault("Settings.Permissions.UsePermissions", false);
        cfg.addDefault("Settings.Permissions.UseBypassPermission", false);
        //Settings: Messages
        cfg.addDefault("Settings.Messages.UseLoginMessage", true);
        cfg.addDefault("Settings.Messages.UseSwitchWorldMessage", true);
        //Settings: Values
        cfg.addDefault("Settings.Values.AttackSpeed", 100);
        cfg.addDefault("Settings.Values.DisabledWorlds", disabledWorlds);
        cfg.addDefault("Settings.Values.RestrictedItems", restrictedItems);
        cfg.addDefault("Settings.Values.CustomItemDamage", customItemDamage);
        //Settings: Features
        cfg.addDefault("Settings.Features.DisableSweepAttacks", true);
        cfg.addDefault("Settings.Features.DisableNewCombatSounds", true);
        cfg.addDefault("Settings.Features.DisablePlayerCollision", true);
        cfg.addDefault("Settings.Features.DisableEnderpearlCooldown", true);
        cfg.addDefault("Settings.Features.CustomItemDamage.EnableCustomItemDamage", false);
        cfg.addDefault("Settings.Features.CustomItemDamage.SendActionBar", true);
        cfg.addDefault("Settings.Features.ItemRestriction.EnableItemRestriction", false);
        cfg.addDefault("Settings.Features.ItemRestriction.SendActionBar", true);
        cfg.addDefault("Settings.Features.ItemRestriction.UseAsWhitelist", false);
        //Settings: UpdateChecker
        cfg.addDefault("Settings.Updates.UseUpdateChecker", true);
        cfg.addDefault("Settings.Updates.ConsoleNotify", true);
        cfg.addDefault("Settings.Updates.IngameNotify", true);

        cfg.addDefault("ConfigVersion", data.getCurrentConfigVersion());

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
        cacheContainer.add(String.class, "ACTIONBAR_PREFIX", cfg.getString("Messages.ActionBarPrefix"));
        cacheContainer.add(String.class, "SWITCH_WORLD_BYPASSED", cfg.getString("Messages.SwitchWorld.Bypassed"));
        cacheContainer.add(String.class, "SWITCH_WORLD_ENABLED", cfg.getString("Messages.SwitchWorld.Enabled"));
        cacheContainer.add(String.class, "SWITCH_WORLD_DISABLED", cfg.getString("Messages.SwitchWorld.Disabled"));
        cacheContainer.add(String.class, "LOGIN_BYPASSED", cfg.getString("Messages.Login.Bypassed"));
        cacheContainer.add(String.class, "LOGIN_ENABLED", cfg.getString("Messages.Login.Enabled"));
        cacheContainer.add(String.class, "LOGIN_DISABLED", cfg.getString("Messages.Login.Disabled"));
        cacheContainer.add(String.class, "ITEM_RESTRICTION_ACTIONBAR_MESSAGE_ENABLED", cfg.getString("Messages.ItemRestriction.ActionBarMessage.Enabled"));
        cacheContainer.add(String.class, "ITEM_RESTRICTION_ACTIONBAR_MESSAGE_DISABLED", cfg.getString("Messages.ItemRestriction.ActionBarMessage.Disabled"));
        cacheContainer.add(String.class, "CUSTOM_ITEM_DAMAGE_ACTIONBAR_MESSAGE", cfg.getString("Messages.CustomItemDamage.ActionBarMessage"));
        cacheContainer.add(String.class, "SETTING_ADD_DISABLED_WORLD", cfg.getString("Messages.Setting.AddDisabledWorld"));
        cacheContainer.add(String.class, "SETTING_REMOVE_DISABLED_WORLD", cfg.getString("Messages.Setting.RemoveDisabledWorld"));
        cacheContainer.add(String.class, "ERROR_WORLD_ALRADY_LISTED", cfg.getString("Messages.Error.WorldAlreadyDisabled"));
        cacheContainer.add(String.class, "ERROR_WORLD_NOT_LISTED", cfg.getString("Messages.Error.WorldAlreadyEnabled"));
        cacheContainer.add(String.class, "ERROR_PLAYER_NOT_ONLINE", cfg.getString("Messages.Error.PlayerNotOnline"));
        cacheContainer.add(String.class, "ERROR_NO_PERMISSIONS", cfg.getString("Messages.Error.NoPerms"));

        //Placeholder
        cacheContainer.add(String.class, "PLACEHOLDER_WORLD_COOLDOWN_ENABLED", cfg.getString("Placeholder.World.CooldownEnabled"));
        cacheContainer.add(String.class, "PLACEHOLDER_WORLD_COOLDOWN_DISABLED", cfg.getString("Placeholder.World.CooldownDisabled"));
        cacheContainer.add(String.class, "PLACEHOLDER_PLAYER_COOLDOWN_ENABLED", cfg.getString("Placeholder.Player.CooldownEnabled"));
        cacheContainer.add(String.class, "PLACEHOLDER_PLAYER_COOLDOWN_DISABLED", cfg.getString("Placeholder.Player.CooldownDisabled"));

        //Settings
        //Settings: Permissions
        cacheContainer.add(Boolean.class, "USE_PERMISSIONS", cfg.getBoolean("Settings.Permissions.UsePermissions"));
        cacheContainer.add(Boolean.class, "USE_BYPASS_PERMISSION", cfg.getBoolean("Settings.Permissions.UseBypassPermission"));
        //Settings: Messages
        cacheContainer.add(Boolean.class, "USE_LOGIN_MESSAGES", cfg.getBoolean("Settings.Messages.UseLoginMessage"));
        cacheContainer.add(Boolean.class, "USE_SWITCH_WORLD_MESSAGES", cfg.getBoolean("Settings.Messages.UseSwitchWorldMessage"));
        //Settings: Values
        cacheContainer.add(Integer.class, "ATTACK_SPEED_VALUE", cfg.getInt("Settings.Values.AttackSpeed"));
        //Settings: Features
        cacheContainer.add(Boolean.class, "DISABLE_SWEEP_ATTACK", cfg.getBoolean("Settings.Features.DisableSweepAttacks"));
        cacheContainer.add(Boolean.class, "DISABLE_NEW_COMBAT_SOUNDS", cfg.getBoolean("Settings.Features.DisableNewCombatSounds"));
        cacheContainer.add(Boolean.class, "DISABLE_PLAYER_COLLISION", cfg.getBoolean("Settings.Features.DisablePlayerCollision"));
        cacheContainer.add(Boolean.class, "DISABLE_ENDERPEARL_COOLDOWN", cfg.getBoolean("Settings.Features.DisableEnderpearlCooldown"));
        cacheContainer.add(Boolean.class, "ENABLE_CUSTOM_ITEM_DAMAGE", cfg.getBoolean("Settings.Features.CustomItemDamage.EnableCustomItemDamage"));
        cacheContainer.add(Boolean.class, "ENABLE_CUSTOM_ITEM_DAMAGE_ACTIONBAR", cfg.getBoolean("Settings.Features.CustomItemDamage.SendActionBar"));
        cacheContainer.add(Boolean.class, "ITEM_RESTRICTION", cfg.getBoolean("Settings.Features.ItemRestriction.EnableItemRestriction"));
        cacheContainer.add(Boolean.class, "ENABLE_ITEM_RESTRICTION_ACTIONBAR", cfg.getBoolean("Settings.Features.ItemRestriction.SendActionBar"));
        cacheContainer.add(Boolean.class, "ITEM_RESTRICTION_AS_WHITELIST", cfg.getBoolean("Settings.Features.ItemRestriction.UseAsWhitelist"));
        //Settings: UpdateChecker
        cacheContainer.add(Boolean.class, "USE_UPDATE_CHECKER", cfg.getBoolean("Settings.Updates.UseUpdateChecker"));
        cacheContainer.add(Boolean.class, "UPDATE_NOTIFY_CONSOLE", cfg.getBoolean("Settings.Updates.ConsoleNotify"));
        cacheContainer.add(Boolean.class, "UPDATE_NOTIFY_INGAME", cfg.getBoolean("Settings.Updates.IngameNotify"));

        //Config Version
        cacheContainer.add(Integer.class, "CONFIG_VERSION", cfg.getInt("ConfigVersion"));

        //Values: RestrictedItems
        for (String restrictedItems : cfg.getStringList("Settings.Values.RestrictedItems")) {
            ItemRestrictionManager.addCache(restrictedItems);
        }

        //Values: DisabledWorlds
        for (String disabledWorld : cfg.getStringList("Settings.Values.DisabledWorlds")) {
            WorldManager.addCache(disabledWorld);
            ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aWorld §e" + disabledWorld + " §adisabled!");
        }

        //Values: CustomItemDamage
        for (String customItemDamage : cfg.getStringList("Settings.Values.CustomItemDamage")) {
            String[] itemParams = customItemDamage.split(":");

            ItemDamageManager.addCache(itemParams[0], itemParams[1]);
        }

        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§aConfig values were successfully cached!");
    }

    public void setDisabledWorld(String world, boolean bol) {
        List<String> disabledWorlds = cfg.getStringList("Settings.Values.DisabledWorlds");

        if(bol) {
            disabledWorlds.add(world);
        }else {
            disabledWorlds.remove(world);
        }

        cfg.set("Settings.Values.DisabledWorlds", disabledWorlds);
        saveCFG();
    }
}
