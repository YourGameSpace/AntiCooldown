package com.yourgamespace.anticooldown.api;

import com.yourgamespace.anticooldown.modules.attackcooldown.utils.CooldownHandler;
import org.bukkit.entity.Player;

public class AntiCooldownApi {

    private final CooldownHandler cooldownHandler = new CooldownHandler();

    public AntiCooldownApi() {

    }

    public void enablePlayerCooldown(Player player) {
        cooldownHandler.enableCooldown(player);
    }

    public void disablePlayerCooldown(Player player) {
        cooldownHandler.disableCooldown(player);
    }
}
