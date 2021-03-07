package de.tubeof.ac.listener;

import de.tubeof.ac.data.Data;
import de.tubeof.ac.enums.SettingsType;
import de.tubeof.ac.main.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SweepAttack implements Listener {

    private Data data = Main.getData();

    @EventHandler
    public void onSweep(EntityDamageByEntityEvent event) {
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;;
        if(!data.getBooleanSettings(SettingsType.DISABLE_SWEEP_ATTACK)) return;
        event.setCancelled(true);
    }
}
