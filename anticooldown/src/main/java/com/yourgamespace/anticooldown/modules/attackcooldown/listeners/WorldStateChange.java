package com.yourgamespace.anticooldown.modules.attackcooldown.listeners;

import com.yourgamespace.anticooldown.api.events.WorldDisableEvent;
import com.yourgamespace.anticooldown.api.events.WorldEnableEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.modules.attackcooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.WorldManager;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.module.ModuleListener;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class WorldStateChange extends ModuleListener {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final CooldownHandler cooldownHandler = new CooldownHandler();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    @EventHandler
    public void onWorldEnable(WorldEnableEvent event) {
        World world = event.getWorld();

        for (Player player : world.getPlayers()) {
            // Check Permissions
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

            // Check if player is permitted
            if (!isPermitted) continue;
            cooldownHandler.disableCooldown(player);
        }
    }

    @EventHandler
    public void onWorldDisable(WorldDisableEvent event) {
        World world = event.getWorld();

        for (Player player : world.getPlayers()) {
            // Check Bypass and Permissions
            boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

            if (!isPermitted) continue;
            if (!isBypassed) cooldownHandler.enableCooldown(player);
        }
    }
}
