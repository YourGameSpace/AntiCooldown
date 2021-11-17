package com.yourgamespace.anticooldown.modules;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.ItemRestrictionManager;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class ItemRestriction extends AntiCooldownModule {

    private static final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private static final WorldManager worldManager = AntiCooldown.getWorldManager();
    private static final CooldownHandler cooldownHandler = new CooldownHandler();

    public ItemRestriction(boolean isProtocolLibRequired, boolean registerBukkitListeners) {
        super(isProtocolLibRequired, registerBukkitListeners);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemHold(PlayerItemHeldEvent event) {
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if (!isPermitted) return;

        // Check if world is disabled
        if (worldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: apply cooldown;
            if (isBypassed) applyCooldown(player, item);
        } else {
            // If world enabled, player permitted and not bypassed: apply cooldown;
            applyCooldown(player, item);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;

        Player player = (Player) event.getPlayer();
        String world = player.getWorld().getName();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if (!isPermitted) return;

        // Check if world is disabled
        if (worldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: apply cooldown;
            if (isBypassed) applyCooldown(player, item);
        } else {
            // If world enabled, player permitted and not bypassed: apply cooldown;
            applyCooldown(player, item);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;

        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if (!isPermitted) return;

        // Check if world is disabled
        if (worldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: apply cooldown;
            if (isBypassed) applyCooldown(player, item);
        } else {
            // If world enabled, player permitted and not bypassed: apply cooldown;
            applyCooldown(player, item);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerSwapHandItemsEvent event) {
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;
        if (event.isCancelled()) return;

        Bukkit.getScheduler().runTaskLater(AntiCooldown.getInstance(), () -> {
            Player player = event.getPlayer();
            String world = player.getWorld().getName();
            ItemStack item = player.getInventory().getItemInMainHand();

            // Check Bypass and Permissions
            boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

            // If not permitted: Return;
            if (!isPermitted) return;

            // Check if world is disabled
            if (worldManager.isWorldDisabled(world)) {
                // If disabled and is bypassed: apply cooldown;
                if (isBypassed) applyCooldown(player, item);
            } else {
                // If world enabled, player permitted and not bypassed: apply cooldown;
                applyCooldown(player, item);
            }
        }, 1);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;
        if (event.isCancelled()) return;

        Bukkit.getScheduler().runTaskLater(AntiCooldown.getInstance(), () -> {
            Player player = event.getPlayer();
            String world = player.getWorld().getName();
            ItemStack item = player.getInventory().getItemInMainHand();

            // Check Bypass and Permissions
            boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

            // If not permitted: Return;
            if (!isPermitted) return;

            // Check if world is disabled
            if (worldManager.isWorldDisabled(world)) {
                // If disabled and is bypassed: apply cooldown;
                if (isBypassed) applyCooldown(player, item);
            } else {
                // If world enabled, player permitted and not bypassed: apply cooldown;
                applyCooldown(player, item);
            }
        }, 1);
    }

    private void applyCooldown(Player player, ItemStack item) {
        if (item == null) {
            if (!cooldownHandler.isCooldownDisabled(player)) {
                cooldownHandler.disableCooldown(player);
                sendActionBar(player);
            }
        } else if (ItemRestrictionManager.isItemRestricted(item.getType())) {
            if (cooldownHandler.isCooldownDisabled(player)) {
                cooldownHandler.enableCooldown(player);
                sendActionBar(player);
            }
        } else if (!cooldownHandler.isCooldownDisabled(player)) {
            cooldownHandler.disableCooldown(player);
            sendActionBar(player);
        }
    }

    private void sendActionBar(Player player) {
        if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ENABLE_ITEM_RESTRICTION_ACTIONBAR"))) {
            String message;
            if (cooldownHandler.isCooldownDisabled(player)) {
                message = ObjectTransformer.getString(cacheContainer.get(String.class, "ITEM_RESTRICTION_ACTIONBAR_MESSAGE_ENABLED"));
            } else {
                message = ObjectTransformer.getString(cacheContainer.get(String.class, "ITEM_RESTRICTION_ACTIONBAR_MESSAGE_DISABLED"));
            }
            message = message.replace("%actionbar_prefix%", ObjectTransformer.getString(cacheContainer.get(String.class, "ACTIONBAR_PREFIX")));

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }
}
