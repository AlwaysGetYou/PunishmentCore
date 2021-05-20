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

public class Mute implements CommandExecutor {
    Punishments p = Punishments.getPlugin(Punishments.class);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("mute")) {
            if (!(sender instanceof Player) || sender.isOp() || sender.hasPermission(Utils.mutePerm())) {
                if (args.length > 0) {
                    String reason = "";
                    if (args.length > 1) {
                        for (int i = 1; i < args.length; i++)
                            reason = reason + args[i] + " ";
                        if (args[1].startsWith("@"))
                            try {
                                MemorySection ms = (MemorySection)this.p.getConfig().getConfigurationSection("predefined_reasons").get(args[1].replace("@", "").trim());
                                ms.getName();
                                sender.sendMessage(ms.getName());
                                List<String> text = ms.getStringList("text");
                                for (int j = 0; j < text.size(); j++) {
                                    if (j > 0) {
                                        reason = reason + " " + ((String)text.get(j)).replace("@" + ms.getName(), "");
                                    } else {
                                        reason = reason + ((String)text.get(j)).replace("@" + ms.getName(), "");
                                    }
                                    reason = reason.replace("@" + ms.getName(), "");
                                }
                            } catch (NullPointerException e) {
                                sender.sendMessage("Unable to find that layout.");
                                return true;
                            }
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
                        if (op == null) {
                            sender.sendMessage(ChatColor.RED + "Invalid player");
                            return true;
                        }
                        PlayerData playerData = new PlayerData(op.getUniqueId());
                        if (reason.contains("-s")) {
                            reason = reason.replace("-s", "");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (Utils.hasPerm(sender))
                                    all.sendMessage(ChatColor.GREEN + op.getName() + " was" + ChatColor.YELLOW + " silently " + ChatColor.GREEN + "permanently muted by " + ChatColor.RED + sender.getName() + ChatColor.GREEN + ".");
                            }
                        } else if (reason.contains("-p") || !reason.contains("-s")) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.mute.success_public").replace("%player%", op.getName()).replace("%sender%", sender.getName()).replace("%time%", "")));
                                reason = reason.replace("-p", "");
                            }
                        }
                        reason = reason.replace("-s", "");
                        reason = reason.replace("-p", "");
                        reason = reason.trim();
                        playerData.setMuted(true, sender.getName(), reason.trim());
                        return true;
                    }
                    if (reason.contains("-s")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (all.isOp() || Utils.hasPerm(all)) {
                                all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.mute.success_silent").replace("%player%", target.getName()).replace("%sender%", sender.getName()).replace("%time%", "")));
                                reason = reason.replace("-s", "");
                            }
                        }
                    } else if (reason.contains("-p") || !reason.contains("-s")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.mute.success_public").replace("%player%", target.getName()).replace("%sender%", sender.getName()).replace("%time%", "")));
                            reason = reason.replace("-p", "");
                        }
                    }
                    if (target.hasPermission("punishmentplus.exempt")) {
                        sender.sendMessage(ChatColor.RED + "You cannot punish this player.");
                        return true;
                    }
                    PlayerData data = new PlayerData(target.getUniqueId());
                    reason = reason.replace("-s", "");
                    reason = reason.replace("-p", "");
                    reason = reason.trim();
                    data.setMuted(true, sender.getName(), reason.trim());
                    target.sendMessage(ChatColor.RED + "You have been permanently muted.\nReason: " + data.getMuteReason());
                    return true;
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.invalid_syntax").replace("%command%", "/mute <player> <reason> <modifiers>")));
                return true;
            }
            Utils.noPerms(sender);
            return true;
        }
        return true;
    }
}
