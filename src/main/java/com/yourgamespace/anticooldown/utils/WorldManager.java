package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.files.PluginConfig;
import com.yourgamespace.anticooldown.main.AntiCooldown;

import java.util.ArrayList;

public class WorldManager {

    public WorldManager() {}

    private final PluginConfig pluginConfig = AntiCooldown.getPluginConfig();
    private final ArrayList<String> disabledWorlds = new ArrayList<>();

    public void disableWorld(String world) {
        disabledWorlds.add(world);
        pluginConfig.setDisabledWorld(world, true);
    }

    public void enableWorld(String world) {
        disabledWorlds.remove(world);
        pluginConfig.setDisabledWorld(world, false);
    }

    public void addCache(String world) {
        disabledWorlds.add(world);
    }

    public boolean isWorldDisabled(String world) {
        return disabledWorlds.contains(world);
    }

    public ArrayList<String> getDisabledWorlds() {
        return disabledWorlds;
    }
}
