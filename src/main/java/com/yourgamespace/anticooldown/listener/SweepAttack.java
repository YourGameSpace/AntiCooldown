package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SweepAttack implements Listener {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    @EventHandler
    public void onSweep(EntityDamageByEntityEvent event) {
        if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_SWEEP_ATTACK"))) return;
        if(!EnumUtils.isValidEnum(EntityDamageEvent.DamageCause.class, "ENTITY_SWEEP_ATTACK")) {
            ccs.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§cDisableSweepAttacks are not supported this server!");
            return;
        }
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;
        event.setCancelled(true);
    }
}
