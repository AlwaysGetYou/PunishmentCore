package com.alwaysgetyou.punishments.Data;

import com.alwaysgetyou.punishments.Punishments;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {

    public File f;
    public FileConfiguration conf;
    public UUID uuid;

    Punishments plugin = Punishments.getPlugin(Punishments.class);

    public PlayerData(UUID uuid) {
        this.f = new File(this.plugin.getDataFolder() + "/playerdata/" + uuid.toString() + ".yml");
        this.conf = YamlConfiguration.loadConfiguration(this.f);
        saveData(this.conf, this.f);
        this.uuid = uuid;
    }

    public void saveData(FileConfiguration conf, File file) {
        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMuted(boolean muted, String executor, String reason) {
        this.conf.set("muteData.isActive", muted);
        this.conf.set("muteData.executor", executor);
        this.conf.set("muteData.reason", reason);
        this.conf.set("muteData.when", System.currentTimeMillis() / 1000L);
        this.conf.set("muteData.expires", 0);
        saveData(this.conf, this.f);
    }

    public void setTempmuted(boolean banned, String executor, String reason, long millis) {
        this.conf.set("muteData.isActive", Boolean.valueOf(banned));
        this.conf.set("muteData.executor", executor);
        this.conf.set("muteData.reason", reason);
        this.conf.set("muteData.when", Long.valueOf(System.currentTimeMillis() / 1000L));
        this.conf.set("muteData.expires", Long.valueOf(System.currentTimeMillis() / 1000L + millis));
        saveData(this.conf, this.f);
    }

    public boolean isMuted() {
        return this.conf.getBoolean("muteData.isActive");
    }

    public String getMuteReason() {
        return this.conf.getString("muteData.reason");
    }

    public String getMuteExecutor() {
        return this.conf.getString("muteData.executor");
    }

    public long getMuteWhen() {
        return this.conf.getLong("muteData.when");
    }

    public long getMuteExpires() {
        return this.conf.getLong("muteData.expires");
    }

    public void setBanned(boolean banned, String executor, String reason) {
        this.conf.set("banData.isActive", Boolean.valueOf(banned));
        this.conf.set("banData.executor", executor);
        this.conf.set("banData.reason", reason);
        this.conf.set("banData.when", Long.valueOf(System.currentTimeMillis() / 1000L));
        this.conf.set("banData.expires", Integer.valueOf(0));
        saveData(this.conf, this.f);
    }

    public void setTempbanned(boolean banned, String executor, String reason, long millis) {
        this.conf.set("banData.isActive", Boolean.valueOf(banned));
        this.conf.set("banData.executor", executor);
        this.conf.set("banData.reason", reason);
        this.conf.set("banData.when", Long.valueOf(System.currentTimeMillis() / 1000L));
        this.conf.set("banData.expires", Long.valueOf(System.currentTimeMillis() / 1000L + millis));
        saveData(this.conf, this.f);
    }

    public boolean isTempbanned() {
        if (getExpires() == 0L)
            return false;
        return true;
    }

    public void setBanned(boolean banned) {
        this.conf.set("banData.isActive", Boolean.valueOf(banned));
        if (!banned)
            this.conf.set("banData.expires", Integer.valueOf(0));
        saveData(this.conf, this.f);
    }

    public boolean isBanned() {
        return this.conf.getBoolean("banData.isActive");
    }

    public String getExecutor() {
        return this.conf.getString("banData.executor");
    }

    public String getReason() {
        try {
            return this.conf.getString("banData.reason");
        } catch (Exception e) {
            return "";
        }
    }

    public long getMillis() {
        return this.conf.getLong("banData.when");
    }

    public long getExpires() {
        return this.conf.getLong("banData.expires");
    }

    public boolean exists() {
        return this.f.exists();
    }

}
