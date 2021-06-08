package com.yourgamespace.anticooldown.commands;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.data.Messages;
import com.yourgamespace.anticooldown.enums.MessageType;
import com.yourgamespace.anticooldown.enums.SettingsType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AntiCooldown implements CommandExecutor {

    private Messages messages = com.yourgamespace.anticooldown.main.AntiCooldown.getMessages();
    private Data data = com.yourgamespace.anticooldown.main.AntiCooldown.getData();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§cYou have to be a player!");
            return true;
        }
        Player player = (Player) commandSender;
        if(args.length > 2) {
            sendUsageMessage(player);
            return true;
        }

        //Admin Area
        if(!(player.hasPermission("anticooldown.settings"))) {
            player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.ERROR_NO_PERMISSIONS));
            return true;
        }

        String arg = args[0];

        if(args.length == 1) {
            if(arg.equalsIgnoreCase("help")) {
                sendUsageMessage(player);
                return true;
            }
            else if(arg.equalsIgnoreCase("listDisabledWorlds")) {
                player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§aDisabled Worlds:");
                for(String world : data.getDisabledWorlds()) {
                    player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§7> §e" + world);
                }
                player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§7##### §cEND OF LIST §7#####");
                return true;
            }
            else if(arg.equalsIgnoreCase("enableWorld")) {
                String world = player.getLocation().getWorld().getName();
                if(!(data.isWorldDisabled(world))) {
                    player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.ERROR_WORLD_NOT_LISTED));
                    return true;
                }

                data.enableWorld(world);
                player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.SETTING_ADD_DISABLED_WORLD).replace("%world%", world));

                World bukkitWorld = Bukkit.getWorld(world);
                if(bukkitWorld == null) return true;
                for(Player worldPlayer : bukkitWorld.getPlayers()) {
                    worldPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(data.getIntegerSettings(SettingsType.ATTACK_SPEED_VALUE));
                }
                return true;
            }
            else if(arg.equalsIgnoreCase("disableWorld")) {
                String world = player.getLocation().getWorld().getName();
                if(data.isWorldDisabled(world)) {
                    player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.ERROR_WORLD_ALRADY_LISTED));
                    return true;
                }

                data.disabledWorld(world);
                player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.SETTING_REMOVE_DISABLED_WORLD).replace("%world%", world));

                World bukkitWorld = Bukkit.getWorld(world);
                if(bukkitWorld == null) return true;
                for(Player worldPlayer : bukkitWorld.getPlayers()) {
                    worldPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                }
                return true;
            }
            else {
                sendUsageMessage(player);
                return true;
            }
        }

        String world = args[1];

        if(args.length == 2) {
            if(arg.equalsIgnoreCase("enableWorld")) {
                if(!(data.isWorldDisabled(world))) {
                    player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.ERROR_WORLD_NOT_LISTED));
                    return true;
                }

                data.enableWorld(world);
                player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.SETTING_REMOVE_DISABLED_WORLD).replace("%world%", world));

                World bukkitWorld = Bukkit.getWorld(world);
                if(bukkitWorld == null) return true;
                for(Player worldPlayer : bukkitWorld.getPlayers()) {
                    worldPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(data.getIntegerSettings(SettingsType.ATTACK_SPEED_VALUE));
                }
                return true;
            }
            else if(arg.equalsIgnoreCase("disableWorld")) {
                if(data.isWorldDisabled(world)) {
                    player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.ERROR_WORLD_ALRADY_LISTED));
                    return true;
                }

                data.disabledWorld(world);
                player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.SETTING_ADD_DISABLED_WORLD).replace("%world%", world));

                World bukkitWorld = Bukkit.getWorld(world);
                if(bukkitWorld == null) return true;
                for(Player worldPlayer : bukkitWorld.getPlayers()) {
                    worldPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                }
                return true;
            }
            else {
                sendUsageMessage(player);
                return true;
            }
        }
        return true;
    }
    
    private void sendUsageMessage(Player player) {
        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§cWrong usage!");
        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§7> §e/anticooldown");
        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§7> §e/anticooldown help");
        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§7> §e/anticooldown listDisabledWorlds");
        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§7> §e/anticooldown enableWorld [<World>]");
        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§7> §e/anticooldown disableWorld [<World>]");
        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§7> §e/anticooldown fixPlayerData [<World>]");
    }
}
