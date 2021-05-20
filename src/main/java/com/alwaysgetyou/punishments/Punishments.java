package com.alwaysgetyou.punishments;

import com.alwaysgetyou.punishments.commands.*;
import com.alwaysgetyou.punishments.events.InventoryMovement;
import com.alwaysgetyou.punishments.events.onChat;
import com.alwaysgetyou.punishments.events.onJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Punishments extends JavaPlugin {

    String prefix = "&8[&4&lPunishments&8]";

    public void onEnable() {
        this.start();
    }

    private void start() {
        PluginManager pm = Bukkit.getPluginManager();
        /*
        Listeners
         */
        pm.registerEvents(new InventoryMovement(), this);
        pm.registerEvents(new onJoin(), this);
        pm.registerEvents(new onChat(), this);
        /*
        Commands
         */
        getCommand("ban").setExecutor(new Ban());
        getCommand("unban").setExecutor(new Unban());
        getCommand("check").setExecutor(new Check());
        getCommand("tempban").setExecutor(new Tempban());
        getCommand("mute").setExecutor(new Mute());
        getCommand("reloadconfig").setExecutor(new ReloadConfig());
        getCommand("tempmute").setExecutor(new Tempmute());
        getCommand("unmute").setExecutor(new Unmute());
        /*
        register
         */
        Bukkit.getLogger().info(this.prefix + " Loading files....");
        saveDefaultConfig();
    }

}
