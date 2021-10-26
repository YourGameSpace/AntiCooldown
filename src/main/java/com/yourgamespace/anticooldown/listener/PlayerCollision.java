package com.yourgamespace.anticooldown.listener;

import com.yourgamespace.anticooldown.data.Data;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.WrapperPlayServerScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerCollision implements Listener {

    private final Data data = AntiCooldown.getData();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Check if ProtocolLib is supported by minecraft version
        if (!data.isProtocolLibInstalled()) return;
        Player player = event.getPlayer();

        WrapperPlayServerScoreboardTeam disableCollisionTeam = new WrapperPlayServerScoreboardTeam();
        disableCollisionTeam.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        disableCollisionTeam.setName("dis-coll");
        disableCollisionTeam.setColor(ChatColor.RESET);
        disableCollisionTeam.setCollisionRule("never");
        disableCollisionTeam.setPlayers(new ArrayList<String>() {{
            add(player.getName());
        }});

        disableCollisionTeam.sendPacket(player);
    }
}
