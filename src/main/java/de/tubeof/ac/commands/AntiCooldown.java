package de.tubeof.ac.commands;

import de.tr7zw.changeme.nbtapi.NBTFile;
import de.tr7zw.changeme.nbtapi.NBTList;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import de.tubeof.ac.data.Messages;
import de.tubeof.ac.enums.MessageType;
import de.tubeof.ac.data.Data;
import de.tubeof.ac.enums.SettingsType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AntiCooldown implements CommandExecutor {

    private Messages messages = de.tubeof.ac.main.AntiCooldown.getMessages();
    private Data data = de.tubeof.ac.main.AntiCooldown.getData();

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

        //Default Plugin Message
        if(args.length == 0) {
            player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§3AntiCooldown §afrom §eTUBEOF §ais running on this server.");
            player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§eDownload §anow for §efree:§6 https://www.spigotmc.org/resources/51321/");
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
            else if(arg.equalsIgnoreCase("fixPlayerData")) {
                ArrayList<String> worlds = new ArrayList<>();
                for(World world : Bukkit.getWorlds()) {
                    worlds.add(world.getName());
                }

                for(String world : worlds) {
                    File playerDataFolder = new File(world + "/playerdata");
                    String[] uuids = playerDataFolder.list();
                    for(String playerDataFileName : uuids) {
                        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§aStarting fix for Playerdata-File §e" + playerDataFileName);

                        try {
                            File playerDataFile = new File(world + "/playerdata/" + playerDataFileName);
                            NBTFile nbtPlayerFile = new NBTFile(playerDataFile);
                            NBTList list = nbtPlayerFile.getCompoundList("Attributes");
                            for (int i = 0; i < list.size(); i++) {
                                NBTListCompound lc = (NBTListCompound) list.get(i);
                                if (lc.getString("Name").equals("generic.attackSpeed")) {
                                    lc.setDouble("Base", 4D);
                                }
                            }
                            nbtPlayerFile.save();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                            player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§cAn error has occurred! A detailed error report can be taken from the log!");
                        }
                    }
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
            else if(arg.equalsIgnoreCase("fixPlayerData")) {
                File playerDataFolder = new File(world + "/playerdata");
                if(!playerDataFolder.exists()) {
                    player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§cNo world with this name could be found!");
                    return true;
                }
                String[] uuids = playerDataFolder.list();
                for(String playerDataFileName : uuids) {
                    player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§aStarting fix for Playerdata-File §e" + playerDataFileName);

                    try {
                        File playerDataFile = new File(world + "/playerdata/" + playerDataFileName);
                        NBTFile nbtPlayerFile = new NBTFile(playerDataFile);
                        NBTList list = nbtPlayerFile.getCompoundList("Attributes");
                        for (int i = 0; i < list.size(); i++) {
                            NBTListCompound lc = (NBTListCompound) list.get(i);
                            if (lc.getString("Name").equals("generic.attackSpeed")) {
                                lc.setDouble("Base", 4D);
                            }
                        }
                        nbtPlayerFile.save();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                        player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§cAn error has occurred! A detailed error report can be taken from the log!");
                    }
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
