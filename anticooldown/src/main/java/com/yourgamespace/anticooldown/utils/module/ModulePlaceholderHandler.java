package com.yourgamespace.anticooldown.utils.module;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class ModulePlaceholderHandler {

    public ModulePlaceholderHandler() {}

    private final HashMap<String, AntiCooldownModule> modulePlaceholderHandlers = new HashMap<>();

    /**
     * Register a new module placeholder.
     * @param placeholder The placerholder, which will be registered
     * @param antiCooldownModule The instance, which want to register the placeholder
     */
    public void registerPlaceholder(String placeholder, AntiCooldownModule antiCooldownModule) {
        modulePlaceholderHandlers.put(placeholder, antiCooldownModule);
    }

    /**
     * Unregister a placeholder.
     * @param placeholder The placeholder to be unregistered
     */
    public void unregisterPlaceholder(String placeholder) {
        modulePlaceholderHandlers.remove(placeholder);
    }

    /**
     * Unregister all placeholders of an instance
     * @param antiCooldownModule The instance from which all placeholders are to be unregistered
     */
    public void unregisterPlaceholders(AntiCooldownModule antiCooldownModule) {
        for (String placeholder : modulePlaceholderHandlers.keySet()) {
            if (modulePlaceholderHandlers.get(placeholder).equals(antiCooldownModule)) {
                modulePlaceholderHandlers.remove(placeholder);
            }
        }
    }

    /**
     * Get all current registered placeholders.
     * @return All placeholders as ArrayList<String>
     */
    public ArrayList<String> getPlaceholders() {
        return new ArrayList<>(modulePlaceholderHandlers.keySet());
    }

    /**
     * Check if a placerholder registered
     * @param placeholder The placeholder to check
     * @return True if placeholder was found otherwise false
     */
    public boolean isPlaceholderRegistered(String placeholder) {
        return modulePlaceholderHandlers.containsKey(placeholder);
    }

    /**
     * Call a module placeholder.
     * @param placeholder The placeholder to be called
     * @param player The player to be delivered
     * @return Final placeholder content or null
     */
    public String callPlaceholder(String placeholder, Player player) {
        return modulePlaceholderHandlers.get(placeholder).onPlaceholder(placeholder, player);
    }

}
