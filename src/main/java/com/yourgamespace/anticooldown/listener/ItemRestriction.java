package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.ItemRestrictionManager;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("ALL")
public class ItemRestriction implements Listener {

    private static final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private static final CooldownHandler cooldownHandler = new CooldownHandler();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemHold(PlayerItemHeldEvent event) {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;
        if(event.isCancelled()) return;

        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS"));

        // If not permitted: Return;
        if(!isPermitted) return;

        // Check if world is disabled
        if (WorldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: apply cooldown;
            if(isBypassed) applyCooldown(player, item);
        } else {
            // If world enabled, player permitted and not bypassed: apply cooldown;
            applyCooldown(player, item);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;

        Player player = (Player) event.getPlayer();
        String world = player.getWorld().getName();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS"));

        // If not permitted: Return;
        if(!isPermitted) return;

        // Check if world is disabled
        if (WorldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: apply cooldown;
            if(isBypassed) applyCooldown(player, item);
        } else {
            // If world enabled, player permitted and not bypassed: apply cooldown;
            applyCooldown(player, item);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;

        Player player = (Player) event.getPlayer();
        String world = player.getWorld().getName();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS"));

        // If not permitted: Return;
        if(!isPermitted) return;

        // Check if world is disabled
        if (WorldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: apply cooldown;
            if(isBypassed) applyCooldown(player, item);
        } else {
            // If world enabled, player permitted and not bypassed: apply cooldown;
            applyCooldown(player, item);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ITEM_RESTRICTION"))) return;
        if(event.isCancelled()) return;

        Bukkit.getScheduler().runTaskLater(AntiCooldown.getInstance(), () -> {
            Player player = event.getPlayer();
            String world = player.getWorld().getName();
            ItemStack item = player.getInventory().getItemInMainHand();

            // Check Bypass and Permissions
            boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
            boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS")) && player.hasPermission("anticooldown.cooldown") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS"));

            // If not permitted: Return;
            if(!isPermitted) return;

            // Check if world is disabled
            if (WorldManager.isWorldDisabled(world)) {
                // If disabled and is bypassed: apply cooldown;
                if(isBypassed) applyCooldown(player, item);
            } else {
                // If world enabled, player permitted and not bypassed: apply cooldown;
                applyCooldown(player, item);
            }
        }, 1);
    }

    private void applyCooldown(Player player, ItemStack item) {
        if(item == null) cooldownHandler.disableCooldown(player);
        else if(ItemRestrictionManager.isItemRestricted(item.getType())) cooldownHandler.enableCooldown(player);
        else if(!cooldownHandler.isCooldownDisabled(player)) cooldownHandler.disableCooldown(player);
    }
}
