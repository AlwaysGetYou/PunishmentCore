package com.alwaysgetyou.punishments.utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class Utils {

    public static void noPerms(final Player player) {
        if (!player.hasPermission(tempmutePerm()) || !player.hasPermission(tempbanPerm()) || !player.hasPermission(banPerm()) || !player.hasPermission(unmutePerm()) || !player.hasPermission(unbanPerm()) || !player.hasPermission(mutePerm()))
            player.sendMessage("§8[§4!§8] §cYou do not have permission to perform this command.");
    }

    public static void noPerms(CommandSender sender) {
        if (!sender.hasPermission(tempmutePerm()) || !sender.hasPermission(tempbanPerm()) || !sender.hasPermission(banPerm()) || !sender.hasPermission(unmutePerm()) || !sender.hasPermission(unbanPerm()) || !sender.hasPermission(mutePerm()))
            sender.sendMessage("§8[§4!§8] §cYou do not have permission to perform this command.");
    }

    public static boolean hasPerm(Player player) {
        return player.hasPermission("punish.alert");
    }

    public static boolean hasPerm(CommandSender sender) {
        return sender.hasPermission("punish.alert");
    }

    public static String tempbanPerm() {
        return "punishment.tempban";
    }

    public static String banPerm() {
        return "punishment.ban";
    }

    public static String mutePerm() {
        return "punish.mute";
    }

    public static String tempmutePerm() {
        return "punish.tempmute";
    }

    public static String unmutePerm() {
        return "punish.unmute";
    }

    public static String unbanPerm() {
        return "punish.unban";
    }

    public String millisToDate(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        int mYear = calendar.get(1);
        int mMonth = calendar.get(2) + 1;
        int mDay = calendar.get(5);
        int mHour = calendar.get(11);
        int mMins = calendar.get(12);
        int mSecs = calendar.get(13);
        String day = String.valueOf(mDay);
        String month = String.valueOf(mMonth);
        String hour = String.valueOf(mHour);
        String min = String.valueOf(mMins);
        String secs = String.valueOf(mSecs);
        if(mDay < 10)
            day = "0" + mDay;
        if(mMins < 10)
            month = "0" + mMonth;
        if (mHour < 10)
            hour = "0" + mHour;
        if (mMins < 10)
            min = "0" + mMins;
        if (mSecs < 10)
            secs = "0" + mSecs;
        String date = day + "/" + month + "/" + mYear;
        String time = hour + ":" + min + ":" + secs;
        return date + " @ " + time;
    }

    public static String formatTime(int seconds) {
        String day = "";
        String hour = "";
        String min = "";
        String sec = "";
        int days = seconds / 86400;
        if (days == 1) {
            day = "1 day";
        } else if (days > 1) {
            day = days + " days";
        }
        seconds -= days * 86400;
        int hours = seconds / 3600;
        if (hours == 1) {
            hour = "1 hour";
        } else if (hours > 1) {
            hour = hours + " hours";
        }
        seconds -= hours * 3600;
        int minutes = seconds / 60;
        if (minutes == 1) {
            min = "1 minute";
        } else if (minutes > 1) {
            min = minutes + " minutes";
        }
        seconds -= minutes * 60;
        if (seconds == 1) {
            sec = "1 second";
        } else if (seconds > 1) {
            sec = seconds + " seconds";
        }
        String fin = day + " " + hour + " " + min + " " + sec;
        if (hour.equals("")) {
            fin = day + " " + min + " " + sec;
            if (min.equals(""))
                fin = day + " " + sec;
        } else if (min.equals("")) {
            fin = day + " " + hour + " " + sec;
        }
        return fin.trim();
    }


}
