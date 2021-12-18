package com.yourgamespace.anticooldown.module.customitemdamage.files;

import com.yourgamespace.anticooldown.module.customitemdamage.main.CustomItemDamage;
import com.yourgamespace.anticooldown.module.customitemdamage.utils.ItemDamageManager;
import com.yourgamespace.anticooldown.utils.module.ModuleConfig;

import java.util.List;

public class Config {

    private final ModuleConfig moduleConfig = new ModuleConfig(CustomItemDamage.getInstance());

    public Config() {}

    public void setupConfig() {
        moduleConfig.getConfig().options().copyDefaults(true);

        //List: Item Damage Values
        List<String> customItemDamage = moduleConfig.getConfig().getStringList("OverrideItemDamage");
        customItemDamage.add("WOOD_AXE:3.0D");
        customItemDamage.add("WOODEN_AXE:3.0D");
        customItemDamage.add("GOLD_AXE:3.0D");
        customItemDamage.add("GOLDEN_AXE:3.0D");
        customItemDamage.add("STONE_AXE:4.0D");
        customItemDamage.add("IRON_AXE:5.0D");
        customItemDamage.add("DIAMOND_AXE:6.0D");
        customItemDamage.add("WOOD_PICKAXE:2.0D");
        customItemDamage.add("WOODEN_PICKAXE:2.0D");
        customItemDamage.add("GOLD_PICKAXE:2.0D");
        customItemDamage.add("GOLDEN_PICKAXE:2.0D");
        customItemDamage.add("STONE_PICKAXE:3.0D");
        customItemDamage.add("IRON_PICKAXE:4.0D");
        customItemDamage.add("DIAMOND_PICKAXE:5.0D");
        customItemDamage.add("WOODEN_SHOVEL:1.0D");
        customItemDamage.add("GOLDEN_SHOVEL:1.0D");
        customItemDamage.add("STONE_SHOVEL:2.0D");
        customItemDamage.add("IRON_SHOVEL:3.0D");
        customItemDamage.add("DIAMOND_SHOVEL:4.0D");
        moduleConfig.getConfig().addDefault("OverrideItemDamage", customItemDamage);

        moduleConfig.saveConfig();
    }

    public void loadConfig() {
        //Values: CustomItemDamage
        for (String customItemDamage : moduleConfig.getConfig().getStringList("OverrideItemDamage")) {
            String[] itemParams = customItemDamage.split(":");
            ItemDamageManager.addCache(itemParams[0], itemParams[1]);
        }
    }
}
