package com.yourgamespace.anticooldown.modules;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.module.AntiCooldownModule;
import com.yourgamespace.anticooldown.utils.module.ModuleDescription;
import de.tubeof.tubetils.api.cache.CacheContainer;
import de.tubeof.tubetils.api.updatechecker.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateNotifyOnJoin extends AntiCooldownModule implements Listener {

    private final UpdateChecker updateChecker = AntiCooldown.getUpdateChecker();
    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public UpdateNotifyOnJoin(boolean isProtocolLibRequired, ModuleDescription moduleDescription) {
        super(isProtocolLibRequired, moduleDescription);
    }

    //@Override
    //public void onEnable() {
    //    registerListener(this);
    //}

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Update Notify
        if (updateChecker != null && player.hasPermission("anticooldown.update") && updateChecker.isOutdated() && ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "UPDATE_NOTIFY_INGAME"))) {
            player.sendMessage(cacheContainer.get(String.class, "PREFIX") + "§cAn update is available! (v" + updateChecker.getLatestVersion() + ") Download here: §e" + updateChecker.getDownloadUrl());
        }
    }
}
