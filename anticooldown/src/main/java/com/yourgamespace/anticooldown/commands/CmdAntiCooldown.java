package com.yourgamespace.anticooldown.commands;

import com.yourgamespace.anticooldown.api.events.WorldDisableEvent;
import com.yourgamespace.anticooldown.api.events.WorldEnableEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public class CmdAntiCooldown implements CommandExecutor {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§cYou have to be a player!");
            return true;
        }
        Player player = (Player) commandSender;
        if (args.length > 2 || args.length == 0) {
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§cWrong usage!");
            sendHelpMessage(player);
            return true;
        }

        //Admin Area
        if (!(player.hasPermission("anticooldown.settings"))) {
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "ERROR_NO_PERMISSIONS")));
            return true;
        }

        String arg = args[0];

        if (args.length == 1) {
            if (arg.equalsIgnoreCase("help")) {
                sendHelpMessage(player);
                return true;
            } else if (arg.equalsIgnoreCase("listDisabledWorlds")) {
                player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§aDisabled Worlds:");
                for (String world : worldManager.getDisabledWorlds()) {
                    player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7> §e" + world);
                }
                player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7##### §cEND OF LIST §7#####");
                return true;
            } else if (arg.equalsIgnoreCase("enableWorld")) {
                String world = player.getLocation().getWorld().getName();
                if (!(worldManager.isWorldDisabled(world))) {
                    player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "ERROR_WORLD_NOT_LISTED")));
                    return true;
                }

                worldManager.enableWorld(world);
                player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SETTING_REMOVE_DISABLED_WORLD")).replace("%world%", world));

                World bukkitWorld = Bukkit.getWorld(world);
                if (bukkitWorld == null) return true;
                Bukkit.getPluginManager().callEvent(new WorldEnableEvent(bukkitWorld));
                return true;
            } else if (arg.equalsIgnoreCase("disableWorld")) {
                String world = player.getLocation().getWorld().getName();
                if (worldManager.isWorldDisabled(world)) {
                    player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "ERROR_WORLD_ALRADY_LISTED")));
                    return true;
                }

                worldManager.disableWorld(world);
                player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SETTING_ADD_DISABLED_WORLD")).replace("%world%", world));

                World bukkitWorld = Bukkit.getWorld(world);
                if (bukkitWorld == null) return true;
                Bukkit.getPluginManager().callEvent(new WorldDisableEvent(bukkitWorld));
                return true;
            } else {
                sendHelpMessage(player);
                return true;
            }
        }

        String world = args[1];

        if (args.length == 2) {
            if (arg.equalsIgnoreCase("enableWorld")) {
                if (!(worldManager.isWorldDisabled(world))) {
                    player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "ERROR_WORLD_NOT_LISTED")));
                    return true;
                }

                worldManager.enableWorld(world);
                player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SETTING_REMOVE_DISABLED_WORLD")).replace("%world%", world));

                World bukkitWorld = Bukkit.getWorld(world);
                if (bukkitWorld == null) return true;
                Bukkit.getPluginManager().callEvent(new WorldEnableEvent(bukkitWorld));
                return true;
            } else if (arg.equalsIgnoreCase("disableWorld")) {
                if (worldManager.isWorldDisabled(world)) {
                    player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "ERROR_WORLD_ALRADY_LISTED")));
                    return true;
                }

                worldManager.disableWorld(world);
                player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SETTING_ADD_DISABLED_WORLD")).replace("%world%", world));

                World bukkitWorld = Bukkit.getWorld(world);
                if (bukkitWorld == null) return true;
                Bukkit.getPluginManager().callEvent(new WorldDisableEvent(bukkitWorld));
                return true;
            } else {
                sendHelpMessage(player);
                return true;
            }
        }
        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7> §e/anticooldown");
        player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7> §e/anticooldown help");
        player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7> §e/anticooldown listDisabledWorlds");
        player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7> §e/anticooldown enableWorld [<World>]");
        player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7> §e/anticooldown disableWorld [<World>]");
    }
}
