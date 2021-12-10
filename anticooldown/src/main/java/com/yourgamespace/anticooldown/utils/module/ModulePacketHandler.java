package com.yourgamespace.anticooldown.utils.module;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class ModulePacketHandler implements PacketListener {

    public abstract void onLoad();

    public abstract void onUnload();

    /**
     * Getter for ProtocolManager of ProtocolLib
     * @return ProtocolManager instance
     */
    public final ProtocolManager getProtocolManager() {
        return AntiCooldown.getProtocolManager();
    }

    /**
     * Default {@link PacketListener} methode.
     */
    public void onPacketSending(PacketEvent packetEvent) {}

    /**
     * Default {@link PacketListener} methode.
     */
    public void onPacketReceiving(PacketEvent packetEvent) {}

    /**
     * Default {@link PacketListener} methode.
     */
    public ListeningWhitelist getSendingWhitelist() {
        return null;
    }

    /**
     * Default {@link PacketListener} methode.
     */
    public ListeningWhitelist getReceivingWhitelist() {
        return null;
    }

    /**
     * Default {@link PacketListener} methode.
     */
    public Plugin getPlugin() {
        return null;
    }
}
