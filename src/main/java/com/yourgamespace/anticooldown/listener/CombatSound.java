package com.yourgamespace.anticooldown.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Sound;

public class CombatSound {

    private static final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public static class PacketHandler {

        public PacketHandler() {
            onSweepAttackSound();
        }

        private void onSweepAttackSound() {
            if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_SWEEP_ATTACK"))) return;

            AntiCooldown.getProtocolManager().addPacketListener(new PacketAdapter(AntiCooldown.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    Sound sound = event.getPacket().getSoundEffects().read(0);
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_SWEEP)) event.setCancelled(true);
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_CRIT)) event.setCancelled(true);
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK)) event.setCancelled(true);
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_STRONG)) event.setCancelled(true);
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE)) event.setCancelled(true);
                }
            });
        }
    }
}
