package com.yourgamespace.anticooldown.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.LoggingHandler;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.VersionHandler;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SweepAttackDamage extends AntiCooldownModule {

    private static final LoggingHandler loggingHandler = AntiCooldown.getLoggingHandler();
    private static final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private static final Data data = AntiCooldown.getData();
    private static final VersionHandler versionHandler = AntiCooldown.getVersionHandler();

    public SweepAttackDamage(boolean isProtocolLibRequired, boolean registerBukkitListeners) {
        super(isProtocolLibRequired, registerBukkitListeners);
    }

    @Override
    public boolean compatibilityTest() {
        if (versionHandler.getVersionId() < 8) {
            loggingHandler.warn("§4WARNING: §cDisableSweepAttacks is not supported by §e" + versionHandler.getMinecraftVersion() + " (" + Bukkit.getBukkitVersion() + "§c!");
            return false;
        }
        return true;
    }

    @Override
    public void onEnable() {
        if (data.isProtocolLibInstalled()) {
            new PacketHandler();
        }
    }

    @EventHandler
    public void onSweepAttackDamage(EntityDamageByEntityEvent event) {
        // Check if feature is supported by minecraft version
        if (AntiCooldown.getVersionHandler().getVersionId() < 8) return;
        // Check if feature is disabled
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_SWEEP_ATTACK"))) return;


        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        String world = player.getWorld().getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.sweepattack") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if (!isPermitted) return;

        // Check if world is disabled
        if (WorldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: disable particles;
            if (isBypassed) event.setCancelled(true);
        } else {
            // If world enabled, player permitted and not bypassed: disable particles;
            event.setCancelled(true);
        }
    }

    public static class PacketHandler {

        public PacketHandler() {
            onSweepAttackParticles();
        }

        private void onSweepAttackParticles() {
            if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_SWEEP_ATTACK"))) return;

            AntiCooldown.getProtocolManager().addPacketListener(new PacketAdapter(AntiCooldown.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_PARTICLES) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    // Check if valid particle
                    boolean valid = false;
                    Particle particle = event.getPacket().getNewParticles().read(0).getParticle();
                    if (particle.equals(Particle.SWEEP_ATTACK)) valid = true;
                    if (particle.equals(Particle.DAMAGE_INDICATOR)) valid = true;

                    // If not valid: Return;
                    if (!valid) return;

                    Player player = event.getPlayer();
                    String world = player.getWorld().getName();

                    // Check Bypass and Permissions
                    boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
                    boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.sweepattack") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

                    // If not permitted: Return;
                    if (!isPermitted) return;

                    // Check if world is disabled
                    if (WorldManager.isWorldDisabled(world)) {
                        // If disabled and is bypassed: disable particles;
                        if (isBypassed) event.setCancelled(true);
                    } else {
                        // If world enabled, player permitted and not bypassed: disable particles;
                        event.setCancelled(true);
                    }
                }
            });
        }
    }
}
