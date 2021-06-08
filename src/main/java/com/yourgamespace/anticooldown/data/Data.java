package com.yourgamespace.anticooldown.data;

import com.yourgamespace.anticooldown.files.PluginConfig;
import com.yourgamespace.anticooldown.enums.SettingsType;
import com.yourgamespace.anticooldown.main.AntiCooldown;

import java.util.ArrayList;

public class Data {

    public Data() {}

    private final PluginConfig pluginConfig = AntiCooldown.getPluginConfig();

    private boolean useSwitchWorldMessage;
    private boolean useLoginMessage;
    private boolean isUpdateAvailable;
    private boolean useUpdateChecker;
    private boolean updateNotifyConsole;
    private boolean updateNotifyIngame;
    private boolean disableSweepAttacks;
    private int attackSpeedValue;
    private int configVersion;
    private ArrayList<String> disabledWorlds = new ArrayList<>();

    public void setBooleanSettings(SettingsType settingsType, boolean bol) {
        if(settingsType == SettingsType.USE_LOGIN_MESSAGES) useLoginMessage = bol;
        if(settingsType == SettingsType.USE_SWITCH_WORLD_MESSAGES) useSwitchWorldMessage = bol;
        if(settingsType == SettingsType.USE_UPDATE_CHECKER) useUpdateChecker = bol;
        if(settingsType == SettingsType.UPDATE_NOTIFY_CONSOLE) updateNotifyConsole = bol;
        if(settingsType == SettingsType.UPDATE_NOTIFY_INGAME) updateNotifyIngame = bol;
        if(settingsType == SettingsType.DISABLE_SWEEP_ATTACK) disableSweepAttacks = bol;
    }

    public void setIntegerSettings(SettingsType settingsType, int intValue) {
        if(settingsType == SettingsType.ATTACK_SPEED_VALUE) attackSpeedValue = intValue;
    }

    public void disabledWorld(String world) {
        disabledWorlds.add(world);
        pluginConfig.setDisabledWorld(world, true);
    }

    public void enableWorld(String world) {
        disabledWorlds.remove(world);
        pluginConfig.setDisabledWorld(world, false);
    }

    public void addDisableWorldToCache(String world) {
        disabledWorlds.add(world);
    }

    public boolean isWorldDisabled(String world) {
        return disabledWorlds.contains(world);
    }

    public boolean getBooleanSettings(SettingsType settingsType) {
        if(settingsType == SettingsType.USE_LOGIN_MESSAGES) return useLoginMessage;
        if(settingsType == SettingsType.USE_SWITCH_WORLD_MESSAGES) return useSwitchWorldMessage;
        if(settingsType == SettingsType.USE_UPDATE_CHECKER) return useUpdateChecker;
        if(settingsType == SettingsType.UPDATE_NOTIFY_INGAME) return updateNotifyIngame;
        if(settingsType == SettingsType.UPDATE_NOTIFY_CONSOLE) return updateNotifyConsole;
        if(settingsType == SettingsType.DISABLE_SWEEP_ATTACK) return disableSweepAttacks;

        return true;
    }

    public int getIntegerSettings(SettingsType settingsType) {
        if(settingsType == SettingsType.ATTACK_SPEED_VALUE) return attackSpeedValue;

        return 0;
    }

    public ArrayList<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public void setUpdateAvailable(boolean bol) {
        isUpdateAvailable = bol;
    }

    public boolean isUpdateAvailable() {
        return isUpdateAvailable;
    }

    public void setConfigVersion(int version) {
        configVersion = version;
    }

    public int getConfigVersion() {
        return configVersion;
    }
}
