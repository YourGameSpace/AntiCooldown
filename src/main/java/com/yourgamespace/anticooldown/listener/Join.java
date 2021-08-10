package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import de.tubeof.tubetils.api.updatechecker.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings("ALL")
public class Join implements Listener {

    private final UpdateChecker updateChecker = AntiCooldown.getUpdateChecker();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final CooldownHandler cooldownHandler = new CooldownHandler();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String world = player.getLocation().getWorld().getName();

        // Update Notify
        if(updateChecker != null && player.hasPermission("anticooldown.update") && updateChecker.isOutdated() && ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "UPDATE_NOTIFY_INGAME"))) {
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§cAn update is available! (v" + updateChecker.getLatestVersion() + ") Download here: §e" + updateChecker.getDownloadUrl());
        }

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        if(!isBypassed && ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS")) && !player.hasPermission("anticooldown.cooldown")) return;

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
}
