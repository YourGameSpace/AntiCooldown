package com.yourgamespace.anticooldown.utils.module;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.AntiCooldownLogger;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ModuleCommandHandler {

    private final AntiCooldownLogger antiCooldownLogger = AntiCooldown.getAntiCooldownLogger();

    public ModuleCommandHandler() {}

    private final HashMap<String, String> moduleCommandDescriptions = new HashMap<>();
    private final HashMap<String, AntiCooldownModule> moduleCommandHandlers = new HashMap<>();

    public void registerCommand(String commandPrefix, String description, AntiCooldownModule antiCooldownModule) {
        moduleCommandDescriptions.put(commandPrefix, description);
        moduleCommandHandlers.put(commandPrefix, antiCooldownModule);
    }

    public void unregisterCommand(String commandPrefix, AntiCooldownModule antiCooldownModule) {
        moduleCommandDescriptions.remove(commandPrefix);
        moduleCommandHandlers.remove(commandPrefix);
    }

    public void unregisterCommands(AntiCooldownModule antiCooldownModule) {
        for (String commandPrefix : moduleCommandHandlers.keySet()) {
            if (moduleCommandHandlers.get(commandPrefix).equals(antiCooldownModule)) {
                moduleCommandHandlers.remove(commandPrefix);
                moduleCommandDescriptions.remove(commandPrefix);
            }
        }
    }

    public boolean callCommand(String commandPrefix, CommandSender commandSender, String[] args) {
        return moduleCommandHandlers.get(commandPrefix).onCommand(commandSender, args);
    }

    public String getDescription(String commandPrefix) {
        return moduleCommandDescriptions.get(commandPrefix);
    }

    public ArrayList<String> getCommands() {
        return new ArrayList<>(moduleCommandHandlers.keySet());
    }

}
