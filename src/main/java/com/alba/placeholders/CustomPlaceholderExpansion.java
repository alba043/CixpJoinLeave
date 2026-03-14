package com.alba.placeholders;

import com.alba.CixpJoinLeave;
import com.alba.utils.ConfigManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomPlaceholderExpansion extends PlaceholderExpansion {

    private final CixpJoinLeave plugin;

    public CustomPlaceholderExpansion(CixpJoinLeave plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cixpjoinleave";
    }

    @Override
    public @NotNull String getAuthor() {
        return "cixp";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // Custom placeholder: %cixpjoinleave_formatted_name%
        if (params.equalsIgnoreCase("formatted_name")) {
            String format = plugin.getConfig().getString("custom-placeholders.player-name-format");
            if (format != null) {
                return ConfigManager.colorize(format.replace("%player_name%", player.getName()));
            }
            return player.getName();
        }

        // Custom placeholder: %cixpjoinleave_join_message%
        if (params.equalsIgnoreCase("join_message")) {
            String format = plugin.getConfig().getString("messages.join.format");
            if (format != null) {
                return ConfigManager.colorize(format.replace("%player_name%", player.getName()));
            }
            return "";
        }

        // Custom placeholder: %cixpjoinleave_leave_message%
        if (params.equalsIgnoreCase("leave_message")) {
            String format = plugin.getConfig().getString("messages.leave.format");
            if (format != null) {
                return ConfigManager.colorize(format.replace("%player_name%", player.getName()));
            }
            return "";
        }

        return null;
    }
}