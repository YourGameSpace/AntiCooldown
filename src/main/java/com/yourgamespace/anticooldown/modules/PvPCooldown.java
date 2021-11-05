package com.yourgamespace.anticooldown.modules;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@SuppressWarnings("ConstantConditions")
public class PvPCooldown extends AntiCooldownModule {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final CooldownHandler cooldownHandler = new CooldownHandler();

    public PvPCooldown(boolean isProtocolLibRequired, boolean registerBukkitListeners) {
        super(isProtocolLibRequired, registerBukkitListeners);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String world = player.getLocation().getWorld().getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if(!isPermitted) return;

        // Check if world is disabled
        if (WorldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed, disable cooldown;
            // If disabled and is not bypassed, do nothing;
            if(isBypassed) {
                cooldownHandler.disableCooldown(player);
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_LOGIN_MESSAGES"))) player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "LOGIN_BYPASSED")));
            } else {
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_LOGIN_MESSAGES"))) player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "LOGIN_DISABLED")));
            }
        } else {
            cooldownHandler.disableCooldown(player);
            if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_LOGIN_MESSAGES"))) player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "LOGIN_ENABLED")));
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
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if(!isPermitted) return;

        // 2 Tick Delay to prevent bugs
        Bukkit.getScheduler().scheduleSyncDelayedTask(AntiCooldown.getInstance(), () -> {
            // Check if world is disabled
            if (WorldManager.isWorldDisabled(world)) {
                // If disabled and is bypassed, disable cooldown;
                // If disabled and is not bypassed, do nothing;
                if(isBypassed) {
                    cooldownHandler.disableCooldown(player);
                    if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_SWITCH_WORLD_MESSAGES")))  player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SWITCH_WORLD_BYPASSED")));
                } else {
                    cooldownHandler.enableCooldown(player);
                    if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_SWITCH_WORLD_MESSAGES")))  player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SWITCH_WORLD_DISABLED")));
                }
            } else {
                cooldownHandler.disableCooldown(player);
                if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_SWITCH_WORLD_MESSAGES"))) player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "SWITCH_WORLD_ENABLED")));
            }
        }, 2);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (cooldownHandler.isCooldownDisabled(player))
            cooldownHandler.enableCooldown(player);
    }
}
