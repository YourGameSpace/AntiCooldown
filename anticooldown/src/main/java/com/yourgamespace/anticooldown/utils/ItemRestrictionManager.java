package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.AntiCooldownLogger;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ItemRestrictionManager {

    private static final AntiCooldownLogger antiCooldownLogger = AntiCooldown.getAntiCooldownLogger();
    private static final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    private static final ArrayList<Material> restrictedItems = new ArrayList<>();

    public static void addCache(String paramMaterial) {
        Material material = null;
        try {
            material = Material.valueOf(paramMaterial);
        } catch (IllegalArgumentException exception) {
            antiCooldownLogger.info("§4WARNING: §cMaterial §e" + paramMaterial + " §cfor ItemRestriction cannot be found or is not supported by §e" + Bukkit.getBukkitVersion() + "§c!");
        }

        restrictedItems.add(material);
    }

    public static boolean isItemRestricted(Material material) {
        boolean bol = restrictedItems.contains(material);

        if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION_AS_WHITELIST"))) {
            return !bol;
        }
        return bol;
    }

    public static ArrayList<Material> getRestrictedItems() {
        return restrictedItems;
    }
}
