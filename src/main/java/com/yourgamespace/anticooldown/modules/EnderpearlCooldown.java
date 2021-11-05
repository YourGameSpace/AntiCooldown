package com.yourgamespace.anticooldown.modules;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EnderpearlCooldown extends AntiCooldownModule {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    @EventHandler
    public void onEnderpearlShoot(ProjectileLaunchEvent event) {
        // Check if feature is supported by minecraft version
        if(AntiCooldown.getVersionHandler().getVersionId() < 8) return;
        // Check if feature is disabled
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_ENDERPEARL_COOLDOWN"))) return;

        // For compatibility with other plugins
        if(event.isCancelled()) return;

        Projectile projectile = event.getEntity();
        if(!(projectile instanceof EnderPearl)) return;
        ProjectileSource projectileSource = projectile.getShooter();
        if(!(projectileSource instanceof Player)) return;
        Player player = (Player) projectileSource;
        String world = player.getWorld().getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.enderpearlcooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if(!isPermitted) return;
        // If world is disable and not bypassed: Return;
        if(WorldManager.isWorldDisabled(world) && !isBypassed) return;

        // Set enderpearl cooldown to 0 (ticks)
        // Will also disable cooldown animation at client
        Bukkit.getScheduler().runTaskLater(AntiCooldown.getInstance(), () -> player.setCooldown(Material.ENDER_PEARL, 0), 0);
    }
}
