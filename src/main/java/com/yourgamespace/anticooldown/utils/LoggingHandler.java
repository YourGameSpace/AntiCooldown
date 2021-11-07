package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import de.tubeof.tubetils.api.cache.CacheContainer;

public class LoggingHandler {

    public LoggingHandler() {}

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public void info(String message) {
        AntiCooldown.getInstance().getLogger().info(cacheContainer.get(String.class, "STARTUP_PREFIX") + message);
    }

    public void warn(String message) {
        AntiCooldown.getInstance().getLogger().warning(cacheContainer.get(String.class, "STARTUP_PREFIX") + message);
    }

    public void debug(Class paramClass, String message) {
        AntiCooldown.getInstance().getLogger().fine(cacheContainer.get(String.class, "STARTUP_PREFIX") + "§7[§6DEBUG§7] §7[§e" + paramClass.getName() + "§7]§f" +  message);
    }
}
