package com.yourgamespace.anticooldown.commands;

import com.yourgamespace.anticooldown.api.events.WorldDisableEvent;
import com.yourgamespace.anticooldown.api.events.WorldEnableEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.WorldManager;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.module.ModuleCommandHandler;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public class CmdAntiCooldown implements CommandExecutor {

    private final ModuleCommandHandler moduleCommandHandler = AntiCooldown.getModuleCommandHandler();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§cYou have to be a player!");
            return true;
        }
        Player player = (Player) commandSender;

        //Admin Area
        if (!(player.hasPermission("anticooldown.settings"))) {
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "ERROR_NO_PERMISSIONS")));
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("help")) {
            int page;
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception) {
                sendHelpMessage(player, 1);
                return true;
            }

            sendHelpMessage(player, page);
            return true;
        } else if (subCommand.equalsIgnoreCase("listDisabledWorlds")) {
            // Command require only 1 arg
            if (args.length != 1) {
                sendHelpMessage(player, 1);
                return true;
            }

            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§aDisabled Worlds:");
            for (String world : worldManager.getDisabledWorlds()) {
                player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7> §e" + world);
            }
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7##### §cEND OF LIST §7#####");
            return true;
        } else if (subCommand.equalsIgnoreCase("enableWorld")) {
            if (args.length == 1) {
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
            } else if (args.length == 2) {
                String world = args[1];
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
            } else {
                // Command can only handle up to 2 args
                sendHelpMessage(player, 1);
                return true;
            }

        } else if (subCommand.equalsIgnoreCase("disableWorld")) {
            if (args.length == 1) {
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
            } else if (args.length == 2) {
                String world = args[1];
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
                // Command can only handle up to 2 args
                sendHelpMessage(player, 1);
                return true;
            }

        } else if (moduleCommandHandler.isCommandPrefixRegistered(subCommand)) {
            // Call module command
            return moduleCommandHandler.callCommand(subCommand, commandSender, args);
        }

        // No command was found
        sendHelpMessage(player, 1);
        return true;
    }

    private void sendHelpMessage(Player player, int page) {
        // Start with 0 in case of 1
        page--;

        ArrayList<String> helpMessages = new ArrayList<>();
        ArrayList<String> moduleCommands = moduleCommandHandler.getCommands();

        int staticPages = 1;
        int maxLinesPerPage = 4;

        // Calculate page count
        int modulePages = (int) Math.ceil((double) moduleCommands.size() / maxLinesPerPage);
        int maxPages = staticPages + modulePages;

        // Validate page index
        if (!(page >= 0 && page <= maxPages)) {
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§cThis help page was not be found!");
            return;
        }

        // Help Page Header
        player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§7§m==== §aHelp-Page §e" + page + "$7/§e" + maxPages + " §7§m====");

        // Add messages to array
        // Static help messages
        helpMessages.add("§7> §e/anticooldown help <Page> §7§oShow command usages");
        helpMessages.add("§7> §e/anticooldown listDisabledWorlds §7§oList all disabled worlds");
        helpMessages.add("§7> §e/anticooldown enableWorld [<World>] §7§oEnable a world");
        helpMessages.add("§7> §e/anticooldown disableWorld [<World>] §7§oDisable a world");
        // Module commands
        for (String moduleCommand : moduleCommands) {
            helpMessages.add("§7> §e/anticooldown " + moduleCommand + " §7§o" + moduleCommandHandler.getDescription(moduleCommand));
        }

        int startIndex = page * maxLinesPerPage;
        for (int cycle = 0; cycle < 3; ++cycle) {
            if (!(helpMessages.size() >= (startIndex + cycle))) break;
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + helpMessages.get(startIndex + cycle));
        }
    }
}
