package com.alwaysgetyou.punishments.commands;

import com.alwaysgetyou.punishments.Data.PlayerData;
import com.alwaysgetyou.punishments.Punishments;
import com.alwaysgetyou.punishments.utilities.Utils;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.List;

public class Tempmute implements CommandExecutor {

    PlayerData data;

    Punishments p = Punishments.getPlugin(Punishments.class);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((label.equalsIgnoreCase("tempmute") || label.equalsIgnoreCase("tmute")) && (
                !(sender instanceof Player) || sender.isOp() || sender.hasPermission(Utils.tempmutePerm()))) {
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
                reason = reason.replace(args[1], "");
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
                                reason = reason.replace("-s", "");
                                TextComponent t = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempmute.success_silent").replace("%player%", op.getName()).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time))));
                                t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&eExecutor: &7" + sender.getName() + "\n&eReason: &7" + reason.trim() + "\n&eSilent: &7True"))).create()));
                                all.spigot().sendMessage((BaseComponent)t);
                            }
                        }
                    } else if (reason.contains("-p") || !reason.contains("-s")) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            reason = reason.replace("-p", "");
                            TextComponent t = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempmute.success_public").replace("%player%", op.getName()).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time))));
                            t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&eExecutor: &7" + sender.getName() + "\n&eReason: &7" + reason.trim() + "\n&eSilent: &7False"))).create()));
                            all.spigot().sendMessage((BaseComponent)t);
                        }
                    }
                    this.data.setTempmuted(true, sender.getName(), reason.trim(), time);
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
                            all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempmute.success_silent").replace("%player%", target.getName()).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time))));
                            reason = reason.replace("-s", "");
                        }
                    }
                } else if (reason.contains("-p") || !reason.contains("-s")) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempmute.success_public").replace("%player%", target.getName()).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time))));
                        reason = reason.replace("-p", "");
                    }
                }
                this.data.setTempmuted(true, sender.getName(), reason.trim(), time);
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.tempmute.target_message").replace("%reason%", reason.trim())).replace("%sender%", sender.getName()).replace("%time%", Utils.formatTime((int)time)));
                return true;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.p.getConfig().getString("messages.invalid_syntax").replace("%command%", "/tempmute <player> <time> <reason> <modifiers>")));
            return true;
        }
        Utils.noPerms(sender);
        return true;
    }
}
