package com.yourgamespace.anticooldown.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SweepAttack implements Listener {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final CooldownHandler cooldownHandler = new CooldownHandler();

    @EventHandler
    public void onSweep(EntityDamageByEntityEvent event) {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_SWEEP_ATTACK"))) return;
        if(!EnumUtils.isValidEnum(EntityDamageEvent.DamageCause.class, "ENTITY_SWEEP_ATTACK")) {
            ccs.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§cDisableSweepAttacks are not supported this server!");
            return;
        }
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        if(cooldownHandler.isCooldownDisabled(player)) event.setCancelled(true);
    }

    public static class ParticleHandler {

        public ParticleHandler() {
            onSweepParticles();
        }

        private void onSweepParticles() {
            AntiCooldown.getProtocolManager().addPacketListener(new PacketAdapter(AntiCooldown.getInstance(), ListenerPriority.NORMAL, PacketType.values()) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    Bukkit.broadcastMessage(event.getPacket().toString());
                }
            });
        }
    }
}
