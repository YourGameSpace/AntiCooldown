package com.yourgamespace.anticooldown.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.WorldManager;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.basics.VersionHandler;
import com.yourgamespace.anticooldown.utils.module.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.module.ModuleDescription;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SweepAttackParticle extends AntiCooldownModule {

    private final VersionHandler versionHandler = AntiCooldown.getVersionHandler();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    public SweepAttackParticle(boolean isProtocolLibRequired, boolean registerBukkitListeners, ModuleDescription moduleDescription) {
        super(isProtocolLibRequired, registerBukkitListeners, moduleDescription);
    }

    // TODO: Added own config option
    // TODO: Add own permissions

    @Override
    public void registerPacketHandler() {
        new PacketHandler();
    }

    public class PacketHandler {

        public PacketHandler() {
            onSweepAttackParticles();
        }

        private void onSweepAttackParticles() {
            if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_SWEEP_ATTACK"))) return;

            AntiCooldown.getProtocolManager().addPacketListener(new PacketAdapter(AntiCooldown.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_PARTICLES) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    Player player = event.getPlayer();
                    String world = player.getWorld().getName();

                    // Check Bypass and Permissions
                    boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
                    boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.sweepattack") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

                    // If not permitted: Return;
                    if (!isPermitted) return;

                    // Validate particle
                    boolean valid = false;
                    if (versionHandler.getVersionId() >= 12) {
                        Particle particle = event.getPacket().getNewParticles().read(0).getParticle();
                        if (particle.equals(Particle.SWEEP_ATTACK)
                        || particle.equals(Particle.DAMAGE_INDICATOR)) valid = true;
                    } else {
                        String particle = event.getPacket().getParticles().read(0).toString();
                        if (particle.equals("SWEEP_ATTACK")
                        || particle.equals("DAMAGE_INDICATOR")) valid = true;
                    }

                    // If particle not valid: Return;
                    if (!valid) return;

                    // Check if world is disabled
                    if (worldManager.isWorldDisabled(world)) {
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
