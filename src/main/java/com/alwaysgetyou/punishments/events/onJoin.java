package com.alwaysgetyou.punishments.events;

import com.alwaysgetyou.punishments.Data.PlayerData;
import com.alwaysgetyou.punishments.utilities.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class onJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerData data = new PlayerData(player.getUniqueId());
        if (data.isTempbanned() && data.getExpires() < System.currentTimeMillis() / 1000L && data.getExpires() != 0L)
            return;
        if (data.isTempbanned())
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', "&cYou are temporarily banned from this server.\n\nReason: " + data
                    .getReason()) + "\nExpires: " + Utils.formatTime((int)((int)data.getExpires() - System.currentTimeMillis() / 1000L)));
        if (data.isBanned() && data.getExpires() == 0L)
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', "&cYou are permanently banned from this server.\n\nReason: " + data
                    .getReason()));
    }
}
