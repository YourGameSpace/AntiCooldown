package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.data.Messages;
import com.yourgamespace.anticooldown.enums.MessageType;
import com.yourgamespace.anticooldown.enums.SettingsType;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SwitchWorld implements Listener {

    private Data data = AntiCooldown.getData();
    private Messages messages = AntiCooldown.getMessages();


    @EventHandler
    public void onWorldTeleport(PlayerTeleportEvent event) {
        if(event.getFrom().getWorld() == event.getTo().getWorld()) return;
        Player player = event.getPlayer();
        String world = event.getTo().getWorld().getName();

        Bukkit.getScheduler().scheduleSyncDelayedTask(AntiCooldown.getInstance(), () -> {
            if (data.isWorldDisabled(world)) {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                if (data.getBooleanSettings(SettingsType.USE_SWITCH_WORLD_MESSAGES))  player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.SWITCH_WORLD_DISABLED));
            } else {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(data.getIntegerSettings(SettingsType.ATTACK_SPEED_VALUE));
                if (data.getBooleanSettings(SettingsType.USE_SWITCH_WORLD_MESSAGES)) player.sendMessage(messages.getTextMessage(MessageType.PREFIX) + messages.getTextMessage(MessageType.SWITCH_WORLD_ENABLED));
            }
        }, 2);
    }
}