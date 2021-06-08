package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.enums.SettingsType;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    private Data data = AntiCooldown.getData();
    private CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String world = player.getLocation().getWorld().getName();

        if (data.isWorldDisabled(world)) {
            if (data.getBooleanSettings(SettingsType.USE_LOGIN_MESSAGES)) player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "LOGIN_DISABLED")));
            return;
        }

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(data.getIntegerSettings(SettingsType.ATTACK_SPEED_VALUE));
        if (data.getBooleanSettings(SettingsType.USE_LOGIN_MESSAGES)) player.sendMessage(cacheContainer.get(String.class, "PREFIX") + ObjectTransformer.getString(cacheContainer.get(String.class, "LOGIN_ENABLED")));

        if(player.hasPermission("anticooldown.update") && data.isUpdateAvailable() && data.getBooleanSettings(SettingsType.UPDATE_NOTIFY_INGAME)) {
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§aAn update is available! Download now: §ehttps://tubeof.de/anticooldown/lastVersion");
        }
    }
}
