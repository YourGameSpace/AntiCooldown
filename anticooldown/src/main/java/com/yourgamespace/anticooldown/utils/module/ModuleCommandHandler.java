package com.yourgamespace.anticooldown.utils.module;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.AntiCooldownLogger;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class ModuleCommandHandler {

    private final AntiCooldownLogger antiCooldownLogger = AntiCooldown.getAntiCooldownLogger();

    public ModuleCommandHandler() {}

    private final HashMap<String, String> moduleCommandDescriptions = new HashMap<>();
    private final HashMap<String, AntiCooldownModule> moduleCommandHandlers = new HashMap<>();

    /**
     * Register a new module command.
     * @param commandPrefix Set command prefix, which will be listed in the help list
     * @param description A short description of the command
     * @param antiCooldownModule The instance, which want to register the command
     */
    public void registerCommand(String commandPrefix, String description, AntiCooldownModule antiCooldownModule) {
        moduleCommandDescriptions.put(commandPrefix, description.replace("§", ""));
        moduleCommandHandlers.put(commandPrefix, antiCooldownModule);
    }

    /**
     * Unregister a command by its command prefix.
     * @param commandPrefix The command prefix to be unregistered
     * @param antiCooldownModule The instance of which the command is to be unregistered
     */
    public void unregisterCommand(String commandPrefix, AntiCooldownModule antiCooldownModule) {
        moduleCommandDescriptions.remove(commandPrefix);
        moduleCommandHandlers.remove(commandPrefix);
    }

    /**
     * Unregister all commands of an instance
     * @param antiCooldownModule The instance from which all commands are to be unregistered
     */
    public void unregisterCommands(AntiCooldownModule antiCooldownModule) {
        for (String commandPrefix : moduleCommandHandlers.keySet()) {
            if (moduleCommandHandlers.get(commandPrefix).equals(antiCooldownModule)) {
                moduleCommandHandlers.remove(commandPrefix);
                moduleCommandDescriptions.remove(commandPrefix);
            }
        }
    }

    /**
     * Get command description.
     * @param commandPrefix The prefix of the command for which the description should be given
     * @return The command description
     */
    public String getDescription(String commandPrefix) {
        return moduleCommandDescriptions.get(commandPrefix);
    }

    /**
     * Get all current registered commands.
     * @return All commands as ArrayList<String>
     */
    public ArrayList<String> getCommands() {
        return new ArrayList<>(moduleCommandHandlers.keySet());
    }

    /**
     * Check if a command prefix registered
     * @param commandPrefix The command prefix to check
     * @return True if command prefix was found otherwise false
     */
    public boolean isCommandPrefixRegistered(String commandPrefix) {
        return moduleCommandHandlers.containsKey(commandPrefix);
    }

    /**
     * Call an module command.
     * @param commandPrefix The command prefix to be called
     * @param commandSender The CommandSender to be delivered
     * @param args Args as String[] to be delivered
     * @return True if command success or false, if failed
     */
    public boolean callCommand(String commandPrefix, CommandSender commandSender, String[] args) {
        return moduleCommandHandlers.get(commandPrefix).onCommand(commandPrefix, commandSender, args);
    }

}
