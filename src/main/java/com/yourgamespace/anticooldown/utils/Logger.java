package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Logger {

    public Logger() {}

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public void print(String message) {
        ccs.sendMessage(cacheContainer.get(String.class, "STARTUP_PREFIX") + message);
    }
}
