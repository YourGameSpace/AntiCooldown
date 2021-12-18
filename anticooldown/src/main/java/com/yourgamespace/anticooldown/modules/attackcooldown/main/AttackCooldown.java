package com.yourgamespace.anticooldown.modules.attackcooldown.main;

import com.yourgamespace.anticooldown.modules.attackcooldown.listeners.JoinQuit;
import com.yourgamespace.anticooldown.modules.attackcooldown.listeners.WorldChange;
import com.yourgamespace.anticooldown.modules.attackcooldown.listeners.WorldStateChange;
import com.yourgamespace.anticooldown.modules.attackcooldown.utils.CooldownHandler;
import com.yourgamespace.anticooldown.utils.module.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.module.ModuleDescription;
import org.bukkit.event.Listener;

public class AttackCooldown extends AntiCooldownModule implements Listener {

    public AttackCooldown(boolean isProtocolLibRequired, ModuleDescription moduleDescription) {
        super(isProtocolLibRequired, moduleDescription);
    }

    private final CooldownHandler cooldownHandler = new CooldownHandler();

    @Override
    public void onEnable() {
        registerListener(new JoinQuit());
        registerListener(new WorldChange());
        registerListener(new WorldStateChange());

        // Reload: Apply cooldown to all online players
        cooldownHandler.setDefaultCooldown();
    }
}
