package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
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



    }
}
