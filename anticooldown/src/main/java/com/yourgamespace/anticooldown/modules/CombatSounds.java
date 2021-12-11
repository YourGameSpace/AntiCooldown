package com.yourgamespace.anticooldown.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.api.events.WorldDisableEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import com.yourgamespace.anticooldown.utils.module.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.module.ModuleDescription;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CombatSounds extends AntiCooldownModule {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    public CombatSounds(boolean isProtocolLibRequired, ModuleDescription moduleDescription) {
        super(isProtocolLibRequired, moduleDescription);
    }

    //@Override
    //public void registerPacketHandler() {
    //    new PacketHandler();
    //}

    public class PacketHandler {

        public PacketHandler() {
            onNewAttackSounds();
        }

        private void onNewAttackSounds() {
            if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_NEW_COMBAT_SOUNDS"))) return;

            AntiCooldown.getProtocolManager().addPacketListener(new PacketAdapter(AntiCooldown.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                @Override
                public void onPacketSending(PacketEvent event) {
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
            });
        }
    }
}
