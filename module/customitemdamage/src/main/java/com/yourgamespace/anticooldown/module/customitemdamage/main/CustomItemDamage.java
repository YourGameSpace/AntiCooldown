package com.yourgamespace.anticooldown.module.customitemdamage.main;

import com.yourgamespace.anticooldown.module.customitemdamage.files.Config;
import com.yourgamespace.anticooldown.module.customitemdamage.listeners.ApplyDamage;
import com.yourgamespace.anticooldown.utils.module.AntiCooldownModule;
import org.bukkit.event.Listener;

public class CustomItemDamage extends AntiCooldownModule implements Listener {

    public CustomItemDamage(boolean isProtocolLibRequired) {
        super(false);
    }

    private static CustomItemDamage instance;
    private static Config config;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config();

        config.setupConfig();
        config.loadConfig();

        registerListener(new ApplyDamage());
    }

    public static CustomItemDamage getInstance() {
        return instance;
    }

    public static Config getConfig() {
        return config;
    }
}
