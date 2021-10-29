package com.yourgamespace.anticooldown.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerCollisionHandler {

    private static final ArrayList<UUID> collisionPlayers = new ArrayList<>();

    public static boolean isCollisionDisabled(Player player) {
        return collisionPlayers.contains(player.getUniqueId());
    }

    public static void enableCollision(Player player) {
        // Create no-collision team delete packet
        WrapperPlayServerScoreboardTeam disableCollisionTeam = new WrapperPlayServerScoreboardTeam();
        disableCollisionTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED);
        disableCollisionTeam.setName("dis-coll");

        // Send packet
        disableCollisionTeam.sendPacket(player);

        // Remove player
        collisionPlayers.remove(player.getUniqueId());
    }

    public static void disableCollision(Player player) {
        // Create no-collision team packet
        WrapperPlayServerScoreboardTeam disableCollisionTeam = new WrapperPlayServerScoreboardTeam();
        disableCollisionTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        disableCollisionTeam.setName("dis-coll");
        disableCollisionTeam.setColor(ChatColor.RESET);
        disableCollisionTeam.setCollisionRule("never");
        disableCollisionTeam.setPlayers(new ArrayList<String>() {{
            add(player.getName());
        }});

        // Send packet
        disableCollisionTeam.sendPacket(player);

        // Add player
        collisionPlayers.add(player.getUniqueId());
    }


}
