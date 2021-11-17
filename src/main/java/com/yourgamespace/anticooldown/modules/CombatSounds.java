package com.yourgamespace.anticooldown.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.UUID;

public class CombatSounds extends AntiCooldownModule {

    private static final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public CombatSounds(boolean isProtocolLibRequired, boolean registerBukkitListeners) {
        super(isProtocolLibRequired, registerBukkitListeners);
    }

    @Override
    public void registerPacketHandler() {
        new PacketHandler();
    }

    private static final ArrayList<UUID> disableSoundsPlayers = new ArrayList<>();

    @EventHandler
    public void onSweepAttackDamage(EntityDamageByEntityEvent event) {
        // Check if feature is disabled
        if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_NEW_COMBAT_SOUNDS"))) return;

        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        String world = player.getWorld().getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.sweepattack") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if (!isPermitted) return;

        // Check if world is disabled
        if (WorldManager.isWorldDisabled(world)) {
            // If disabled and is bypassed: disable sounds;
            if (isBypassed) {
                disableSoundsPlayers.add(player.getUniqueId());
                // Remove player from disableSoundsPlayers after 1 tick, if the attack did not trigger any sounds.
                Bukkit.getScheduler().runTaskLaterAsynchronously(AntiCooldown.getInstance(), () -> disableSoundsPlayers.remove(player.getUniqueId()), 1);
            }
        } else {
            // If world enabled, player permitted and not bypassed: disable sounds;
            disableSoundsPlayers.add(player.getUniqueId());
            // Remove player from disableSoundsPlayers after 1 tick, if the attack did not trigger any sounds.
            Bukkit.getScheduler().runTaskLaterAsynchronously(AntiCooldown.getInstance(), () -> disableSoundsPlayers.remove(player.getUniqueId()), 1);
        }
    }

    public static class PacketHandler {

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

                    // Check if sounds disable for player
                    Player player = event.getPlayer();
                    if (!disableSoundsPlayers.contains(player.getUniqueId())) return;

                    // Disable sounds
                    event.setCancelled(true);
                    disableSoundsPlayers.remove(player.getUniqueId());
                }
            });
        }
    }
}
