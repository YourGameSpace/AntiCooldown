package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.files.PluginConfig;
import com.yourgamespace.anticooldown.main.AntiCooldown;

import java.util.ArrayList;

public class WorldManager {

    private static final PluginConfig pluginConfig = AntiCooldown.getPluginConfig();
    private static final ArrayList<String> disabledWorlds = new ArrayList<>();

    public static void disableWorld(String world) {
        disabledWorlds.add(world);
        pluginConfig.setDisabledWorld(world, true);
    }

    public static void enableWorld(String world) {
        disabledWorlds.remove(world);
        pluginConfig.setDisabledWorld(world, false);
    }

    public static void addCache(String world) {
        disabledWorlds.add(world);
    }

    public static boolean isWorldDisabled(String world) {
        return disabledWorlds.contains(world);
    }

    public static ArrayList<String> getDisabledWorlds() {
        return disabledWorlds;
    }
}
