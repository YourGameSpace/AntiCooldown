package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerCollision implements Listener {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final Data data = AntiCooldown.getData();

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
        // If world is disable and not bypassed: Return;
        if(WorldManager.isWorldDisabled(world) && !isBypassed) return;


    }
}
