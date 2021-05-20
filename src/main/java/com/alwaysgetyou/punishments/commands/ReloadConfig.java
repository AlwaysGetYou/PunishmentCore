package com.alwaysgetyou.punishments.commands;

import com.alwaysgetyou.punishments.Punishments;
import com.alwaysgetyou.punishments.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfig implements CommandExecutor {

    Punishments p = Punishments.getPlugin(Punishments.class);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("reloadconfig")) {
            if(!(sender instanceof Player) || sender.isOp())
                this.p.reloadConfig();
            Utils.noPerms(sender);
            return true;
        }
        return true;
    }

}
