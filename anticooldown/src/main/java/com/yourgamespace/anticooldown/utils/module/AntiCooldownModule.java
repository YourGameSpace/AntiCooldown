package com.yourgamespace.anticooldown.utils.module;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.AntiCooldownLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

@SuppressWarnings("unused")
public abstract class AntiCooldownModule implements Listener {

    private final AntiCooldownLogger logger = AntiCooldown.getAntiCooldownLogger();
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private final boolean isProtocolLibRequired;
    private final boolean registerBukkitListeners;
    private boolean isEnabled;
    private ModuleDescription moduleDescription;


    public AntiCooldownModule(boolean isProtocolLibRequired, boolean registerBukkitListeners) {
        this.isProtocolLibRequired = isProtocolLibRequired;
        this.registerBukkitListeners = registerBukkitListeners;
    }

    /**
     * Get description from module.
     * @return Module description
     */
    public ModuleDescription getDescription() {
        return this.moduleDescription;
    }

    /**
     * Set module description.
     * @param description Module description
     */
    public void setDescription(ModuleDescription description) {
        this.moduleDescription = description;
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
     * Handle module command methode
     * @param commandPrefix The invoked command prefix
     * @param commandSender The command sender
     * @param args Used args of the command
     * @return Returns true if command success or false if failed
     */
    public boolean onCommand(String commandPrefix, CommandSender commandSender, String[] args) {
        return false;
    }

    /**
     * Check if this module is enabled.
     *
     * @return Returns true, if module is enabled and false if not.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Set the status of this module. Status can be set to enabled and disabled using this methode.
     * WARNING: This will NOT disable/enable the module.
     *
     * @param enabled true for enabled and false for disabled.
     */
    private void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    /**
     * If necessary, possibility to register packet handler.
     */
    public void registerPacketHandler() {}

    /**
     * Will enable this module.
     */
    public void enableModule() {
        if (registerBukkitListeners) pluginManager.registerEvents(this, AntiCooldown.getInstance());
        registerPacketHandler();
        onEnable();

        setEnabled(true);
        logger.info("§aModule §e" + getDescription().getName() + " §asuccessfully enabled!");
    }

    /**
     * Will disable this module.
     */
    public void disableModule() {
        setEnabled(false);

        onDisable();
        HandlerList.unregisterAll(this);

        logger.info("§aModule §e" + getDescription().getName() + " §asuccessfully disabled!");
    }

    /**
     * Will disable the module with the given reason.
     *
     * @param reason The reason, why the module was disabled.
     */
    public void disableModule(String reason) {
        setEnabled(false);

        onDisable();
        HandlerList.unregisterAll(this);

        logger.info("§aModule §e" + getDescription().getName() + " §asuccessfully disabled! §eReason: " + reason);
    }

    /**
     * If necessary, possibility for a self-test.
     *
     * @return Returns true, if self-test passed or false if failed.
     */
    public boolean compatibilityTest() {
        return true;
    }

    /**
     * Getter for isProtocolLibRequired.
     *
     * @return Returns if ProtocolLib is required for the module.
     */
    public boolean isProtocolLibRequired() {
        return isProtocolLibRequired;
    }

}
