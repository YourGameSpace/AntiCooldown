package com.yourgamespace.anticooldown.modules;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.PlayerCollisionHandler;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerCollision extends AntiCooldownModule {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final Data data = AntiCooldown.getData();

    @Override
    public boolean compatibilityTest() {
        if(!data.isProtocolLibInstalled()) return false;

        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Check if ProtocolLib is installed
        if (!data.isProtocolLibInstalled()) return;
        // Check if feature is disabled
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_PLAYER_COLLISION"))) return;

        Player player = event.getPlayer();
        String world = player.getWorld().getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.enderpearlcooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if(!isPermitted) return;

        // Check if world is disabled
        if(WorldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed, disable collisions;
            // If disabled and is not bypassed, enable collisions;
            if(isBypassed) {
                PlayerCollisionHandler.disableCollisions(player);
            } else {
                PlayerCollisionHandler.enableCollisions(player);
            }
        } else {
            PlayerCollisionHandler.disableCollisions(player);
        }
    }

    @EventHandler
    public void onWorldTeleport(PlayerTeleportEvent event) {
        // Check if world was changed; If not: Return
        if(event.getFrom().getWorld() == event.getTo().getWorld()) return;

        Player player = event.getPlayer();
        String world = event.getTo().getWorld().getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.collisions") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if(!isPermitted) return;

        // 2 Tick Delay to prevent bugs
        Bukkit.getScheduler().scheduleSyncDelayedTask(AntiCooldown.getInstance(), () -> {
            // Check if world is disabled
            if(WorldManager.isWorldDisabled(world)) {
                // If disabled and is bypassed, disable collisions;
                // If disabled and is not bypassed, enable collisions;
                if(isBypassed) {
                    PlayerCollisionHandler.disableCollisions(player);
                } else {
                    PlayerCollisionHandler.enableCollisions(player);
                }
            } else {
                PlayerCollisionHandler.disableCollisions(player);
            }

        }, 2);
    }
}
