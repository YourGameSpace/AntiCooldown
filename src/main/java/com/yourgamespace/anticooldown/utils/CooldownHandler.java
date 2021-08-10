package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import de.tubeof.tubetils.api.cache.CacheContainer;
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
}
