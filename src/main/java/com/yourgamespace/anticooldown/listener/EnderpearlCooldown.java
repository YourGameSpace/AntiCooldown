package com.yourgamespace.anticooldown.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.CooldownHandler;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.lang.reflect.InvocationTargetException;

public class EnderpearlCooldown implements Listener {

    private final Data data = AntiCooldown.getData();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final CooldownHandler cooldownHandler = new CooldownHandler();

    @EventHandler
    public void onEnderpearlShoot(ProjectileLaunchEvent event) {
        if(event.isCancelled()) return;

        Projectile projectile = event.getEntity();
        if(!(projectile instanceof EnderPearl)) return;
        ProjectileSource projectileSource = projectile.getShooter();
        if(!(projectileSource instanceof Player)) return;
        Player player = (Player) projectileSource;

        // Set cooldown for enderpearl using bukkit if supported otherwise sending as packet
        if(AntiCooldown.getVersionHandler().getVersionId() >= 8) {
            Bukkit.getScheduler().runTaskLater(AntiCooldown.getInstance(), () -> player.setCooldown(Material.ENDER_PEARL, 0), 0);
        } else if(data.isProtocolLibInstalled()) {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_COOLDOWN);
            packet.getIntegers().write(0, 0);
            packet.getModifier().write(0, CraftItemStack.asNMSCopy(new ItemStack(Material.ENDER_PEARL)).getItem());

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
