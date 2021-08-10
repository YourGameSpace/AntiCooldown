package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.utils.CooldownHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("ALL")
public class Quit implements Listener {

    private final CooldownHandler cooldownHandler = new CooldownHandler();

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (cooldownHandler.isCooldownDisabled(player))
            cooldownHandler.enableCooldown(player);
    }
}
