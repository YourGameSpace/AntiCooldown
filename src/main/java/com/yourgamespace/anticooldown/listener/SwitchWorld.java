package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SwitchWorld implements Listener {

    private final Data data = AntiCooldown.getData();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    @EventHandler
    public void onWorldTeleport(PlayerTeleportEvent event) {
        if(event.getFrom().getWorld() == event.getTo().getWorld()) return;
        Player player = event.getPlayer();
        String world = event.getTo().getWorld().getName();

        Bukkit.getScheduler().scheduleSyncDelayedTask(AntiCooldown.getInstance(), () -> {
            if (worldManager.isWorldDisabled(world)) {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_SWITCH_WORLD_MESSAGES")))  player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SWITCH_WORLD_DISABLED")));
            } else {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ObjectTransformer.getInteger(cacheContainer.get(Integer.class, "ATTACK_SPEED_VALUE")));
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_SWITCH_WORLD_MESSAGES"))) player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SWITCH_WORLD_ENABLED")));
            }
        }, 2);
    }
}