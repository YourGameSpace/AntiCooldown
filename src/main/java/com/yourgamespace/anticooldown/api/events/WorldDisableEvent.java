package com.yourgamespace.anticooldown.api.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class WorldDisableEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final World world;

    public WorldDisableEvent(World world) {
        this.world = world;
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
     * Returns the world involved in this event
     *
     * @return Returns the world object
     */
    public World getWorld() {
        return world;
    }
}
