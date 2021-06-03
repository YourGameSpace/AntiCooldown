package de.tubeof.ac.listener;

import de.tubeof.ac.data.Data;
import de.tubeof.ac.data.Messages;
import de.tubeof.ac.enums.MessageType;
import de.tubeof.ac.enums.SettingsType;
import de.tubeof.ac.main.AntiCooldown;
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
            ccs.sendMessage(messages.getTextMessage(MessageType.PREFIX) + "$cDisableSweepAttacks are not supported this server!");
            return;
        }
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;;
        event.setCancelled(true);
    }
}
