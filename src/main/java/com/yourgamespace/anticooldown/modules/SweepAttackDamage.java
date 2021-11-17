package com.yourgamespace.anticooldown.modules;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.LoggingHandler;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.VersionHandler;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SweepAttackDamage extends AntiCooldownModule {

    private final LoggingHandler loggingHandler = AntiCooldown.getLoggingHandler();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final VersionHandler versionHandler = AntiCooldown.getVersionHandler();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

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

    @EventHandler
    public void onSweepAttackDamage(EntityDamageByEntityEvent event) {
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
        if (worldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: disable particles;
            if (isBypassed) event.setCancelled(true);
        } else {
            // If world enabled, player permitted and not bypassed: disable particles;
            event.setCancelled(true);
        }
    }
}
