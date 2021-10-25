package com.yourgamespace.anticooldown.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.InvocationTargetException;

public class PlayerCollision implements Listener {

    private final Data data = AntiCooldown.getData();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Check if ProtocolLib is installed
        if(!data.isProtocolLibInstalled()) return;

        Player player = event.getPlayer();

        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getIntegers().write(1, 0); // Mode
        packet.getStrings().write(0, "disable-collision"); // Name
        packet.getStrings().write(5, "never"); // Collision rule

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
