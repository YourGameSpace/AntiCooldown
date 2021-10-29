package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

@SuppressWarnings({"ConstantConditions", "unused"})
public class CooldownHandler {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public CooldownHandler() {}

    public boolean isCooldownDisabled(Player player) {
        return player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() != 4;
    }

    public void enableCooldown(Player player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
    }

    public void disableCooldown(Player player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ObjectTransformer.getInteger(cacheContainer.get(Integer.class, "ATTACK_SPEED_VALUE")));
    }

    public void setDefaultCooldown() {
        CooldownHandler cooldownHandler = new CooldownHandler();

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(cooldownHandler.isCooldownDisabled(player)) cooldownHandler.enableCooldown(player);
        }
    }

    public void setOnlinePlayersCooldown() {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String world = onlinePlayer.getLocation().getWorld().getName();

            // Check Bypass and Permissions
            boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && onlinePlayer.hasPermission("anticooldown.bypass");
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && onlinePlayer.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

            // If not permitted: Return;
            if(!isPermitted) return;

            if(WorldManager.isWorldDisabled(world)) {
                // If disabled and is bypassed, disable cooldown;
                // If disabled and is not bypassed, do nothing;
                if(isBypassed) disableCooldown(onlinePlayer);
                else enableCooldown(onlinePlayer);
            } else {
                disableCooldown(onlinePlayer);
            }
        }
    }
}
