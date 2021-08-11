package com.yourgamespace.anticooldown.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SweepAttack implements Listener {

    private static final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final CooldownHandler cooldownHandler = new CooldownHandler();

    @EventHandler
    public void onSweep(EntityDamageByEntityEvent event) {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_SWEEP_ATTACK"))) return;
        if(!EnumUtils.isValidEnum(EntityDamageEvent.DamageCause.class, "ENTITY_SWEEP_ATTACK")) {
            ccs.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§4WARNING: §cDisableSweepAttacks is not supported by §e" + Bukkit.getBukkitVersion() + "§c!");
            return;
        }
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        if(cooldownHandler.isCooldownDisabled(player)) event.setCancelled(true);
    }

    public static class PacketHandler {

        public PacketHandler() {
            onSweepAttackParticles();
        }

        private void onSweepAttackParticles() {
            if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_SWEEP_ATTACK"))) return;

            AntiCooldown.getProtocolManager().addPacketListener(new PacketAdapter(AntiCooldown.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_PARTICLES) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    // Check if valid particle
                    boolean valid = false;
                    Particle particle = event.getPacket().getNewParticles().read(0).getParticle();
                    if(particle.equals(Particle.SWEEP_ATTACK)) valid = true;
                    if(particle.equals(Particle.DAMAGE_INDICATOR)) valid = true;

                    // If not valid: Return;
                    if(!valid) return;

                    Player player = event.getPlayer();
                    String world = player.getWorld().getName();

                    // Check Bypass and Permissions
                    boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
                    boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS")) && player.hasPermission("anticooldown.sweepattack") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS"));

                    // If not permitted: Return;
                    if(!isPermitted) return;

                    // Check if world is disabled
                    if (WorldManager.isWorldDisabled(world) && isBypassed) {
                        // If disabled and is bypassed: disable particles;
                        event.setCancelled(true);
                    } else {
                        // If permitted and not bypassed: disable particles;
                        event.setCancelled(true);
                    }
                }
            });
        }
    }
}
