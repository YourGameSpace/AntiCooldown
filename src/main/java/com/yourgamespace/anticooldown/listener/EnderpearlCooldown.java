package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EnderpearlCooldown implements Listener {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final CooldownHandler cooldownHandler = new CooldownHandler();

    @EventHandler
    public void onEnderpearlShoot(ProjectileLaunchEvent event) {
        // Check if feature is disabled
        // START TEMP DISABLED
        //if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_ENDERPEARL_COOLDOWN"))) return;
        // END TEMP DISABLED
        // Check if feature is supported by minecraft version
        if(AntiCooldown.getVersionHandler().getVersionId() < 8) return;
        if(event.isCancelled()) return;

        Projectile projectile = event.getEntity();
        if(!(projectile instanceof EnderPearl)) return;
        ProjectileSource projectileSource = projectile.getShooter();
        if(!(projectileSource instanceof Player)) return;
        Player player = (Player) projectileSource;

        Bukkit.getScheduler().runTaskLater(AntiCooldown.getInstance(), () -> player.setCooldown(Material.ENDER_PEARL, 0), 0);
    }
}
