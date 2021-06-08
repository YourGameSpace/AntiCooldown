package com.yourgamespace.anticooldown.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class TubeTilsManager {

    private String version;
    private Plugin runningPlugin;

    public TubeTilsManager(Plugin plugin, String version, boolean autoRun) {
        this.version = version;
        this.runningPlugin = plugin;

        if(autoRun) check();
    }

    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private final Plugin plugin = pluginManager.getPlugin("TubeTils");

    @SuppressWarnings("ConstantConditions")
    public void check() {
        if(!isInstalled()) {
            download(version);
            enablePlugin();
            return;
        }

        if(isInstalled()) {
            if(!getVersion().equals(version)) download(version);
        }
    }

    public String getVersion() {
        return plugin != null ? plugin.getDescription().getVersion() : null;
    }

    private boolean isInstalled() {
        if(plugin == null) return false;
        else return true;
    }

    private float downloadProgress = 0;
    private void download(String version) {
        try {
            URL url = new URL("https://hub.yourgamespace.com/repo/de/tubeof/TubeTils/" + version + "/TubeTils-" + version + ".jar");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "TubeApiBridgeConnector");
            connection.setRequestProperty("Header-Token", "SD998FS0FG07");
            int filesize = connection.getContentLength();

            Timer timer = new Timer();
            Thread thread = new Thread(() -> {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        ccs.sendMessage("Downloading TubeTils ...  " + (int)downloadProgress + "%");
                    }
                };
                timer.schedule(timerTask, 0, 250);
            });
            thread.start();

            float totalDataRead = 0;
            BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
            FileOutputStream fos = new java.io.FileOutputStream("plugins/TubeTils.jar");
            BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
            byte[] data = new byte[1024];
            int i = 0;

            while((i=in.read(data,0,1024))>=0) {
                totalDataRead=totalDataRead+i;
                bout.write(data,0,i);
                downloadProgress = (totalDataRead*100) / filesize;
            }
            timer.cancel();
            thread.interrupt();
            ccs.sendMessage("Downloading TubeTils ... " + (int)downloadProgress + "%");

            bout.close();
            in.close();

        } catch (IOException exception) {
            ccs.sendMessage("Error while downloading TubeTils! Disabling plugin ...");
            exception.printStackTrace();

            pluginManager.disablePlugin(runningPlugin);
        }
    }

    private void enablePlugin() {
        try {
            File file = new File("plugins/TubeTils.jar");
            Plugin tubetils = pluginManager.loadPlugin(file);
            pluginManager.enablePlugin(tubetils);
        } catch (InvalidPluginException | InvalidDescriptionException exception) {
            ccs.sendMessage("Error while enabling TubeTils! Disabling plugin ...");
            exception.printStackTrace();

            pluginManager.disablePlugin(runningPlugin);
        }
    }

}
