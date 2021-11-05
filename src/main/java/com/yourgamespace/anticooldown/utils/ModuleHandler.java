package com.yourgamespace.anticooldown.utils;

import java.util.ArrayList;

public class ModuleHandler {

    public ModuleHandler() {}

    private ArrayList<AntiCooldownModule> enabledModules = new ArrayList<>();

    public ArrayList<AntiCooldownModule> getEnabledModules() {
        return enabledModules;
    }

    public void registerModule(AntiCooldownModule antiCooldownModule) {
        if(antiCooldownModule.compatibilityTest()) antiCooldownModule.enableModule();
        else {
            System.out.println("ERROR WHILE ENABLING!!!");
        }
    }
}
