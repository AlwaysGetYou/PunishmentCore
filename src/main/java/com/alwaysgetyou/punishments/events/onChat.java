package com.alwaysgetyou.punishments.events;

import com.alwaysgetyou.punishments.Data.PlayerData;
import com.alwaysgetyou.punishments.Punishments;
import com.alwaysgetyou.punishments.utilities.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onChat implements Listener {
    Punishments p = Punishments.getPlugin(Punishments.class);

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData data = new PlayerData(player.getUniqueId());
        if (data.isMuted() && data.getMuteExpires() < System.currentTimeMillis() / 1000L && data.getMuteExpires() != 0L)
            return;
        if (data.isMuted() && data.getMuteExpires() != 0L) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempmute.sender_message_fail").replace("%reason%", data.getMuteReason()).replace("%sender%", data.getMuteExecutor()).replace("%time%", Utils.formatTime((int)(data.getMuteExpires() - System.currentTimeMillis() / 1000L)))));
            return;
        }
        if (data.isMuted() && data.getMuteExpires() == 0L) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.mute.sender_message_fail").replace("%reason%", data.getMuteReason()).replace("%sender%", data.getMuteExecutor()).replace("%time%", Utils.formatTime((int)data.getMuteExpires()))));
        }
    }
}

