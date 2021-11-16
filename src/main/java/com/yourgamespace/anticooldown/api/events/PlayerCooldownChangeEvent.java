package com.yourgamespace.anticooldown.api.events;

import com.yourgamespace.anticooldown.utils.CooldownHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PlayerCooldownChangeEvent extends Event {

    private static final CooldownHandler cooldownHandler = new CooldownHandler();
    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public PlayerCooldownChangeEvent(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Returns the player involved in this event.
     *
     * @return Returns the player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the new cooldown status of the player.
     *
     * @return Returns true if cooldown is disabled or true, if enabled.
     */
    public boolean isCooldownDisabled() {
        return cooldownHandler.isCooldownDisabled(player);
    }
}
