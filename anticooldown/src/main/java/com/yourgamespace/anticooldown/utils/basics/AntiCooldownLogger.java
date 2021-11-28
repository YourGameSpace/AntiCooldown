package com.yourgamespace.anticooldown.utils.basics;

import com.yourgamespace.anticooldown.main.AntiCooldown;

public class AntiCooldownLogger {

    public AntiCooldownLogger() {
    }

    public void info(String message) {
        AntiCooldown.getInstance().getLogger().info(message);
    }

    public void warn(String message) {
        AntiCooldown.getInstance().getLogger().warning(message);
    }

    public void debug(Class paramClass, String message) {
        AntiCooldown.getInstance().getLogger().fine("[DEBUG] [" + paramClass.getName() + "]§r " + message);
    }
}
