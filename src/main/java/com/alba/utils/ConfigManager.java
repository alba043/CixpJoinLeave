package com.alba.utils;

import com.alba.CixpJoinLeave;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final CixpJoinLeave plugin;
    private FileConfiguration config;

    public ConfigManager(CixpJoinLeave plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public static String colorize(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Component colorizeComponent(String message) {
        if (message == null) return Component.empty();
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    public String getJoinMessage() {
        return colorize(config.getString("messages.join.format", "&a%player_name% joined"));
    }

    public String getLeaveMessage() {
        return colorize(config.getString("messages.leave.format", "&c%player_name% left"));
    }

    public String getFirstJoinMessage() {
        return colorize(config.getString("messages.first-join.format", "&b%player_name% joined for the first time!"));
    }

    public boolean isJoinEnabled() {
        return config.getBoolean("messages.join.enabled", true);
    }

    public boolean isLeaveEnabled() {
        return config.getBoolean("messages.leave.enabled", true);
    }

    public boolean isFirstJoinEnabled() {
        return config.getBoolean("messages.first-join.enabled", true);
    }
}