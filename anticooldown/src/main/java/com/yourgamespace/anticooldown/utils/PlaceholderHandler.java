package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import de.tubeof.tubetils.api.cache.CacheContainer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ConstantConditions")
public class PlaceholderHandler extends PlaceholderExpansion {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final CooldownHandler cooldownHandler = new CooldownHandler();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    @Override
    public @NotNull String getIdentifier() {
        return "anticooldown";
    }

    @Override
    public @NotNull String getAuthor() {
        return "YourGameSpace";
    }

    @Override
    public @NotNull String getVersion() {
        return AntiCooldown.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String placeholder) {
        if (offlinePlayer == null) return null;
        Player player = offlinePlayer.getPlayer();

        if (placeholder.equalsIgnoreCase("worldcooldown")) {
            if (worldManager.isWorldDisabled(player.getWorld().getName())) {
                // World disabled = Cooldown enabled: Return enabled;
                return ObjectTransformer.getString(cacheContainer.get(String.class, "PLACEHOLDER_WORLD_COOLDOWN_ENABLED"));
            }
            // World enabled = Cooldown disabled: Return disabled;
            return ObjectTransformer.getString(cacheContainer.get(String.class, "PLACEHOLDER_WORLD_COOLDOWN_DISABLED"));
        }
        if (placeholder.equalsIgnoreCase("playercooldown")) {
            if (cooldownHandler.isCooldownDisabled(player)) {
                // Cooldown disabled
                return ObjectTransformer.getString(cacheContainer.get(String.class, "PLACEHOLDER_PLAYER_COOLDOWN_DISABLED"));
            }
            // Cooldown enabled
            return ObjectTransformer.getString(cacheContainer.get(String.class, "PLACEHOLDER_PLAYER_COOLDOWN_ENABLED"));
        }


        return null;
    }
}
