package com.yourgamespace.anticooldown.modules.attackcooldown.listeners;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.modules.attackcooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.WorldManager;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.module.ModuleListener;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class JoinQuit extends ModuleListener {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final CooldownHandler cooldownHandler = new CooldownHandler();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String world = Objects.requireNonNull(player.getLocation().getWorld()).getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if (!isPermitted) return;

        // Check if world is disabled
        if (worldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed, disable cooldown;
            // If disabled and is not bypassed, do nothing;
            if (isBypassed) {
                cooldownHandler.disableCooldown(player);
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_LOGIN_MESSAGES"))) {
                    player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "LOGIN_BYPASSED")));
                }
            } else {
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_LOGIN_MESSAGES"))) {
                    player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "LOGIN_DISABLED")));
                }
            }
        } else {
            cooldownHandler.disableCooldown(player);
            if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_LOGIN_MESSAGES"))) {
                player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "LOGIN_ENABLED")));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (cooldownHandler.isCooldownDisabled(player)) {
            cooldownHandler.enableCooldown(player);
        }
    }
}
