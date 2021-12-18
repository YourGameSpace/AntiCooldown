package com.yourgamespace.anticooldown.module.customitemdamage.main;

import com.yourgamespace.anticooldown.module.customitemdamage.listeners.ApplyDamage;
import com.yourgamespace.anticooldown.utils.module.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.module.ModuleDescription;
import org.bukkit.event.Listener;

public class CustomItemDamage extends AntiCooldownModule implements Listener {

    public CustomItemDamage(boolean isProtocolLibRequired, ModuleDescription moduleDescription) {
        super(isProtocolLibRequired, moduleDescription);
    }

    @Override
    public void onEnable() {
        registerListener(new ApplyDamage());
    }
}
