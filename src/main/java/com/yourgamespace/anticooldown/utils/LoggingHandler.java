package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class LoggingHandler {

    public LoggingHandler() {}

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public void info(String message) {
        Bukkit.getLogger().info(cacheContainer.get(String.class, "STARTUP_PREFIX") + message);
    }

    public void warn(String message) {
        Bukkit.getLogger().warning(cacheContainer.get(String.class, "STARTUP_PREFIX") + message);
    }

    public void debug(Class paramClass, String message) {
        Bukkit.getLogger().fine(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§7[§6DEBUG§7] §7[§e" + paramClass.getName() + "§7]§f" +  message);
    }
}
