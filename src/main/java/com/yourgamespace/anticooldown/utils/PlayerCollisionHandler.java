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

    public static void enableCollisions(Player player) {
        // If collisions is not disabled: Return;
        if (!collisionPlayers.contains(player.getUniqueId())) return;

        // Create no-collision team delete packet
        WrapperPlayServerScoreboardTeam disableCollisionTeam = new WrapperPlayServerScoreboardTeam();
        disableCollisionTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED);
        disableCollisionTeam.setName("dis-coll");

        // Send packet
        disableCollisionTeam.sendPacket(player);

        // Remove player
        collisionPlayers.remove(player.getUniqueId());
    }

    public static void disableCollisions(Player player) {
        // If collisions already disabled: Return;
        if (collisionPlayers.contains(player.getUniqueId())) return;

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
