package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.api.events.PlayerCooldownChangeEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

@SuppressWarnings({"ConstantConditions", "unused"})
public class CooldownHandler {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public CooldownHandler() {
    }

    public boolean isCooldownDisabled(Player player) {
        return player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() != 4;
    }

    public void enableCooldown(Player player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
        Bukkit.getPluginManager().callEvent(new PlayerCooldownChangeEvent(player));
    }

    public void disableCooldown(Player player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ObjectTransformer.getInteger(cacheContainer.get(Integer.class, "ATTACK_SPEED_VALUE")));
        Bukkit.getPluginManager().callEvent(new PlayerCooldownChangeEvent(player));
    }

    public void setDefaultCooldown() {
        CooldownHandler cooldownHandler = new CooldownHandler();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (cooldownHandler.isCooldownDisabled(player)) {
                cooldownHandler.enableCooldown(player);
                Bukkit.getPluginManager().callEvent(new PlayerCooldownChangeEvent(player));
            }
        }
    }

    public void setOnlinePlayersCooldown() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String world = player.getLocation().getWorld().getName();

            // Check Bypass and Permissions
            boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

            // If not permitted: Return;
            if (!isPermitted) return;

            if (WorldManager.isWorldDisabled(world)) {
                // If disabled and is bypassed, disable cooldown;
                // If disabled and is not bypassed, do nothing;
                if (isBypassed) {
                    disableCooldown(player);
                    Bukkit.getPluginManager().callEvent(new PlayerCooldownChangeEvent(player));
                } else {
                    enableCooldown(player);
                    Bukkit.getPluginManager().callEvent(new PlayerCooldownChangeEvent(player));
                }
            } else {
                disableCooldown(player);
                Bukkit.getPluginManager().callEvent(new PlayerCooldownChangeEvent(player));
            }
        }
    }
}
