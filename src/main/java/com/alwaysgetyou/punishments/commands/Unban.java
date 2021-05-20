package com.alwaysgetyou.punishments.commands;

import com.alwaysgetyou.punishments.Data.PlayerData;
import com.alwaysgetyou.punishments.Punishments;
import com.alwaysgetyou.punishments.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unban implements CommandExecutor {

    Punishments p = Punishments.getPlugin(Punishments.class);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("unban")) {
            if (!(sender instanceof Player) || sender.isOp() || sender.hasPermission(Utils.unbanPerm())) {
                if (args.length >= 1) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "That player does not exist");
                        return true;
                    }
                    String reason = "";
                    if (args.length > 1)
                        for (int i = 1; i < args.length; i++)
                            reason = reason + args[i] + " ";
                    PlayerData data = new PlayerData(target.getUniqueId());
                    if (data.isBanned()) {
                        data.setBanned(false);
                        data.saveData(data.conf, data.f);
                        if (reason.contains("-s")) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (all.isOp() || Utils.hasPerm(all))
                                    all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.unban.success_silent").replace("%player%", target.getName()).replace("%sender%", sender.getName()).replace("%time%", "")));
                            }
                        } else if (reason.contains("-p") || !reason.contains("-s")) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.unban.success_public").replace("%player%", target.getName()).replace("%sender%", sender.getName()).replace("%time%", "")));
                                reason = reason.replace("-p", "");
                            }
                        }
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "That player is not banned.");
                    return true;
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.invalid_syntax").replace("%command%", "/unban <player> <modifiers>")));
                return true;
            }
            Utils.noPerms(sender);
            return true;
        }
        return true;
    }

}
