package com.yourgamespace.anticooldown.utils.module;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;

@SuppressWarnings("unused")
public abstract class ModulePacketHandler {

    private final ListenerPriority listenerPriority;
    private final PacketType[] packetTypes;

    public ModulePacketHandler(ListenerPriority listenerPriority, PacketType... packetTypes) {
        this.listenerPriority = listenerPriority;
        this.packetTypes = packetTypes;
    }

    public void onLoad() {}

    public void onUnload() {}

    public void onSending(PacketEvent event) {}

    public void onReceiving(PacketEvent event) {}

    public final PacketAdapter sendingAdapter() {
        return new PacketAdapter(AntiCooldown.getInstance(), listenerPriority, packetTypes) {
            @Override
            public void onPacketSending(PacketEvent event) {
                onSending(event);
            }
        };
    }

    public final PacketAdapter receivingAdapter() {
        return new PacketAdapter(AntiCooldown.getInstance(), listenerPriority, packetTypes) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                onReceiving(event);
            }
        };
    }

    /**
     * Getter for ProtocolManager of ProtocolLib
     * @return ProtocolManager instance
     */
    public final ProtocolManager getProtocolManager() {
        return AntiCooldown.getProtocolManager();
    }
}
