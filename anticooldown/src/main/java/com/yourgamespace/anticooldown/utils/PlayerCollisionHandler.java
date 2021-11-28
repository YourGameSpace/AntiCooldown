package com.yourgamespace.anticooldown.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.VersionHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@SuppressWarnings({"unused"})
public class PlayerCollisionHandler {

    private static final VersionHandler versionHandler = AntiCooldown.getVersionHandler();
    private static final ProtocolManager protocolManager = AntiCooldown.getProtocolManager();

    private static final ArrayList<UUID> collisionPlayers = new ArrayList<>();

    public static boolean isCollisionDisabled(Player player) {
        return collisionPlayers.contains(player.getUniqueId());
    }

    public static void enableCollisions(Player player) {
        // If collisions is not disabled: Return;
        if (!collisionPlayers.contains(player.getUniqueId())) return;

        // Create new packet
        PacketContainer enableCollisionPacket = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        if (versionHandler.getVersionId() >= 12) {
            // 1.13+ Packet
            // Set mode: Remove
            enableCollisionPacket.getIntegers().write(0, 1);
            // Set team name
            enableCollisionPacket.getStrings().write(0, "dis-coll");
        } else {
            // Below 1.13 Packet
            // Set mode: Remove
            enableCollisionPacket.getIntegers().write(1, 1);
            // Set team name
            enableCollisionPacket.getStrings().write(0, "dis-coll");
        }

        try {
            protocolManager.sendServerPacket(player, enableCollisionPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Remove player
        collisionPlayers.remove(player.getUniqueId());
    }

    public static void disableCollisions(Player player) {
        // If collisions already disabled: Return;
        if (collisionPlayers.contains(player.getUniqueId())) return;

        // Create new packet
        PacketContainer disableCollisionPacket = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        if (versionHandler.getVersionId() >= 12) {
            // 1.13+ Packet
            // Set mode: Create
            disableCollisionPacket.getIntegers().write(0, 0);
            // Set team name
            disableCollisionPacket.getStrings().write(0, "dis-coll");
            // Set team color: Reset
            disableCollisionPacket.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, ChatColor.RESET);
            // Set collision rule: never
            disableCollisionPacket.getStrings().write(2, "never");
            // Add player to team
            disableCollisionPacket.getSpecificModifier(Collection.class).write(0, new ArrayList<String>() {{
                add(player.getName());
            }
            });
        } else {
            // Below 1.13 Packet
            // Set mode: Create
            disableCollisionPacket.getIntegers().write(1, 0);
            // Set team name
            disableCollisionPacket.getStrings().write(0, "dis-coll");
            // Set team color: Reset
            disableCollisionPacket.getIntegers().write(0, -1);
            // Set collision rule: never
            disableCollisionPacket.getStrings().write(5, "never");
            // Add player to team
            disableCollisionPacket.getSpecificModifier(Collection.class).write(0, new ArrayList<String>() {{
                add(player.getName());
            }
            });
        }

        try {
            protocolManager.sendServerPacket(player, disableCollisionPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Add player
        collisionPlayers.add(player.getUniqueId());
    }


}
