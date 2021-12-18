package com.yourgamespace.anticooldown.module.combatsounds.main;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.yourgamespace.anticooldown.module.combatsounds.packethandler.DisableSounds;
import com.yourgamespace.anticooldown.utils.module.AntiCooldownModule;

public class CombatSounds extends AntiCooldownModule {

    public CombatSounds(boolean isProtocolLibRequired) {
        super(true);
    }

    @Override
    public void onEnable() {
        registerPacketHandler(new DisableSounds(ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT));
    }
}
