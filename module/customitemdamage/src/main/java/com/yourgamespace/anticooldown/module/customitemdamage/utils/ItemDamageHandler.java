package com.yourgamespace.anticooldown.module.customitemdamage.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings({"ConstantConditions", "unused"})
public class ItemDamageHandler {

    private final Player player;
    private final Entity entity;
    private double baseDamage = 0.0D;
    private double enchantmentDamage = 0.0D;

    public ItemDamageHandler(Player player, Entity entity) {
        this.player = player;
        this.entity = entity;

        calculateDamage();
    }

    private static boolean canCriticalHit(Player player) {
        Material blockMaterial = player.getLocation().getBlock().getType();

        return !player.isOnGround()
                && !player.isSprinting()
                && !player.isInsideVehicle()
                && player.getVelocity().getY() < 0.0D
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS)
                && !blockMaterial.equals(Material.LADDER)
                && !blockMaterial.equals(Material.VINE)
                && !blockMaterial.equals(Material.WATER);
    }

    private static boolean isMonsterTypeArthropod(Entity entity) {
        EntityType entityType = entity.getType();
        return entityType.equals(EntityType.SPIDER)
                || entityType.equals(EntityType.CAVE_SPIDER)
                || entityType.equals(EntityType.BEE)
                || entityType.equals(EntityType.SILVERFISH)
                || entityType.equals(EntityType.ENDERMITE);
    }

    private static boolean isMonsterTypeUndead(Entity entity) {
        EntityType entityType = entity.getType();
        return entityType.equals(EntityType.SKELETON)
                || entityType.equals(EntityType.ZOMBIE)
                || entityType.equals(EntityType.ZOMBIE_VILLAGER)
                || entityType.equals(EntityType.WITHER)
                || entityType.equals(EntityType.WITHER_SKELETON)
                || entityType.equals(EntityType.ZOMBIFIED_PIGLIN)
                || entityType.equals(EntityType.SKELETON_HORSE)
                || entityType.equals(EntityType.ZOMBIE_HORSE)
                || entityType.equals(EntityType.STRAY)
                || entityType.equals(EntityType.HUSK)
                || entityType.equals(EntityType.PHANTOM)
                || entityType.equals(EntityType.DROWNED)
                || entityType.equals(EntityType.ZOGLIN);
    }

    private void calculateDamage() {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        baseDamage = ItemDamageManager.getItemDamage(itemStack.getType());

        // Potion Effects
        baseDamage += player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) ? (player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() + 1) * 3 : 0;
        baseDamage -= player.hasPotionEffect(PotionEffectType.WEAKNESS) ? (player.getPotionEffect(PotionEffectType.WEAKNESS).getAmplifier() + 1) * 4 : 0;
        baseDamage *= canCriticalHit(player) ? 1.5D : 1.0D;

        // Enchantments
        if (itemStack.containsEnchantment(Enchantment.DAMAGE_ALL)) {
            int level = itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
            enchantmentDamage += level > 0 ? 1.0D + 0.5D * (level - 1) : 0;
        }
        if (itemStack.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS) && isMonsterTypeArthropod(entity)) {
            int level = itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
            enchantmentDamage += 2.5D * level;
        }
        if (itemStack.containsEnchantment(Enchantment.DAMAGE_UNDEAD) && isMonsterTypeUndead(entity)) {
            int level = itemStack.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);
            enchantmentDamage += 2.5D * level;
        }
    }

    public double getFinalDamage() {
        return baseDamage + enchantmentDamage;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public double getEnchantmentDamage() {
        return enchantmentDamage;
    }
}
