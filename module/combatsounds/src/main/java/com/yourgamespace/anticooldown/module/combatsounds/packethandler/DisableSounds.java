package com.yourgamespace.anticooldown.module.combatsounds.packethandler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.WorldManager;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.module.ModulePacketHandler;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DisableSounds extends ModulePacketHandler {

    public DisableSounds(ListenerPriority listenerPriority, PacketType... packetTypes) {
        super(listenerPriority, packetTypes);
    }

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    @Override
    public void onSending(PacketEvent event) {
        // Check if valid sound
        boolean valid = false;
        Sound sound = event.getPacket().getSoundEffects().read(0);
        if (sound.equals(Sound.ENTITY_PLAYER_ATTACK_SWEEP)) valid = true;
        if (sound.equals(Sound.ENTITY_PLAYER_ATTACK_CRIT)) valid = true;
        if (sound.equals(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK)) valid = true;
        if (sound.equals(Sound.ENTITY_PLAYER_ATTACK_STRONG)) valid = true;
        if (sound.equals(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE)) valid = true;

        // If not valid: Return;
        if (!valid) return;

        // Check if player has permissions
        Player player = event.getPlayer();
        String world = player.getWorld().getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.combatsounds") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if (!isPermitted) return;

        // Check if world is disabled
        if (worldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: disable sounds;
            if (isBypassed) event.setCancelled(true);
        } else {
            // If permitted and not bypassed: disable sounds;
            event.setCancelled(true);
        }
    }
}
