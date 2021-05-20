package com.alwaysgetyou.punishments.commands;

import java.util.List;

import com.alwaysgetyou.punishments.Data.PlayerData;
import com.alwaysgetyou.punishments.Punishments;
import com.alwaysgetyou.punishments.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

public class Tempban implements CommandExecutor {
    PlayerData data;

    Punishments p = Punishments.getPlugin(Punishments.class);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("tempban") || label.equalsIgnoreCase("tban")) {
            if (!(sender instanceof Player) || sender.isOp() || sender.hasPermission(Utils.tempbanPerm())) {
                if (args.length >= 3) {
                    String format = args[1].substring((int)args[1].chars().count() - 1, (int)args[1].chars().count());
                    int duration = Integer.parseInt(args[1].substring(0, (int)args[1].chars().count() - 1));
                    long time = 0L;
                    switch (format) {
                        case "s":
                            time = duration;
                            break;
                        case "m":
                            time = (duration * 60);
                            break;
                        case "h":
                            time = (duration * 60 * 60);
                            break;
                        case "d":
                            time = (duration * 60 * 60 * 24);
                            break;
                        case "w":
                            time = (duration * 60 * 60 * 24 * 7);
                            break;
                        case "y":
                            time = (duration * 60 * 60 * 24 * 365);
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "That is an invalid time type (s, m, h, d, w, y)");
                            return true;
                    }
                    String reason = "";
                    for (int i = 2; i < args.length; i++)
                        reason = reason + args[i] + " ";
                    if (args[2].startsWith("@"))
                        try {
                            MemorySection ms = (MemorySection)this.p.getConfig().getConfigurationSection("predefined_reasons").get(args[2].replace("@", "").trim());
                            ms.getName();
                            sender.sendMessage(ms.getName());
                            List<String> text = ms.getStringList("text");
                            for (int b = 0; b < text.size(); b++) {
                                if (b > 0) {
                                    reason = reason + " " + ((String)text.get(b)).replace("@" + ms.getName(), "");
                                } else {
                                    reason = reason + ((String)text.get(b)).replace("@" + ms.getName(), "");
                                }
                                reason = reason.replace("@" + ms.getName(), "");
                            }
                        } catch (NullPointerException e) {
                            sender.sendMessage("Unable to find that layout.");
                            return true;
                        }
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
                        if (op == null) {
                            sender.sendMessage(ChatColor.RED + "That player is invalid.");
                            return true;
                        }
                        this.data = new PlayerData(op.getUniqueId());
                        if (reason.contains("-s")) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (all.isOp() || Utils.hasPerm(all)) {
                                    all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempban.success_silent").replace("%player%", op.getName()).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time))));
                                    reason = reason.replace("-s", "");
                                }
                            }
                        } else if (reason.contains("-p") || !reason.contains("-s")) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempban.success_public").replace("%player%", op.getName()).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time))));
                                reason = reason.replace("-p", "");
                            }
                        }
                        this.data.setTempbanned(true, sender.getName(), reason, time);
                        this.data.saveData(this.data.conf, this.data.f);
                        return true;
                    }
                    if (target.hasPermission("punishmentplus.exempt")) {
                        sender.sendMessage(ChatColor.RED + "You cannot punish this player.");
                        return true;
                    }
                    this.data = new PlayerData(target.getUniqueId());
                    if (reason.contains("-s")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (all.isOp() || Utils.hasPerm(all)) {
                                all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempban.success_silent").replace("%player%", target.getName()).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time))));
                                reason = reason.replace("-s", "");
                            }
                        }
                    } else if (reason.contains("-p") || !reason.contains("-s")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempban.success_public").replace("%player%", target.getName()).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time))));
                            reason = reason.replace("-p", "");
                        }
                    }
                    target.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYour account has been temporarily suspended from this server.\n\nExpires: " + Utils.formatTime((int)time)));
                    this.data.setTempbanned(true, sender.getName(), reason.trim().replace("-s", "").replace("-p", ""), time);
                    return true;
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.invalid_syntax").replace("%command%", "/tempban <player> <time> <reason> <modifiers>")));
                return true;
            }
            Utils.noPerms(sender);
            return true;
        }
        return true;
    }
}
