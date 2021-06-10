package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("ALL")
public class ItemRestriction implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemHold(PlayerItemHeldEvent event) {
        if(event.isCancelled()) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if(item == null) return;

        Bukkit.broadcastMessage(item.toString());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null) return;

        Bukkit.broadcastMessage(item.toString());
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if(event.isCancelled()) return;

        Bukkit.getScheduler().runTaskLater(AntiCooldown.getInstance(), () -> {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item == null) return;

            Bukkit.broadcastMessage(item.toString());
        }, 1);
    }
}
