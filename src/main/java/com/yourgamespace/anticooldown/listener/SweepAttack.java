package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.data.Messages;
import com.yourgamespace.anticooldown.enums.MessageType;
import com.yourgamespace.anticooldown.enums.SettingsType;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SweepAttack implements Listener {

    private final Data data = AntiCooldown.getData();
    private final Messages messages = AntiCooldown.getMessages();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    @EventHandler
    public void onSweep(EntityDamageByEntityEvent event) {
        if(!data.getBooleanSettings(SettingsType.DISABLE_SWEEP_ATTACK)) return;
        if(!EnumUtils.isValidEnum(EntityDamageEvent.DamageCause.class, "ENTITY_SWEEP_ATTACK")) {
            ccs.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "§cDisableSweepAttacks are not supported this server!");
            return;
        }
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;;
        event.setCancelled(true);
    }
}
