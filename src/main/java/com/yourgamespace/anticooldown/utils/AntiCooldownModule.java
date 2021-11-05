package com.yourgamespace.anticooldown.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

@SuppressWarnings("unused")
public abstract class AntiCooldownModule implements Listener {

    private final Logger logger = AntiCooldown.logger();
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private final String moduleName = getClass().getSimpleName();

    private boolean isEnabled;

    /**
     * Set the status of this module. Status can be set to enabled and disabled using this methode.
     * WARNING: This will NOT disable/enable the module.
     * @param enabled true for enabled and false for disabled.
     */
    private void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    /**
     * If necessary, possibility to run code when the module will be enabled.
     */
    public void onEnable() {}

    /**
     * If necessary, possibility to run code when the module will be disabled.
     */
    public void onDisable() {}

    /**
     * Check if this module is enabled.
     * @return Returns true, if module is enabled and false if not.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Will enabled this module.
     */
    public void enableModule() {
        pluginManager.registerEvents(this, AntiCooldown.getInstance());
        onEnable();

        setEnabled(true);
        logger.print("§aModule §e" + moduleName + " §asuccessfully enabled!");
    }

    /**
     * Will disable this module.
     */
    public void disableModule() {
        setEnabled(false);

        onDisable();
        HandlerList.unregisterAll(this);

        logger.print("§aModule §e" + moduleName + " §asuccessfully disabled!");
    }

    /**
     * If necessary, possibility for a self-test.
     * @return Returns true, if self-test passed or false if failed.
     */
    public boolean compatibilityTest() {
        return true;
    }

}
