package com.alba.listeners;

import com.alba.CixpJoinLeave;
import com.alba.utils.ConfigManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    private final CixpJoinLeave plugin;
    private final ConfigManager configManager;

    public JoinLeaveListener(CixpJoinLeave plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Check if join messages are enabled
        if (!plugin.getConfig().getBoolean("messages.join.enabled")) {
            event.joinMessage(null);
            return;
        }

        // Check if first join
        boolean firstJoin = !player.hasPlayedBefore();
        String format;

        if (firstJoin && plugin.getConfig().getBoolean("messages.first-join.enabled")) {
            format = plugin.getConfig().getString("messages.first-join.format");
        } else {
            format = plugin.getConfig().getString("messages.join.format");
        }

        // Process placeholders
        String message = processPlaceholders(player, format);

        // Set join message using Component
        if (plugin.getConfig().getBoolean("messages.join.broadcast")) {
            event.joinMessage(ConfigManager.colorizeComponent(message));
        } else {
            event.joinMessage(null);
            player.sendMessage(ConfigManager.colorizeComponent(message));
        }

        // Send title if enabled
        if (plugin.getConfig().getBoolean("messages.join.title.enabled")) {
            String title = processPlaceholders(player, plugin.getConfig().getString("messages.join.title.title"));
            String subtitle = processPlaceholders(player, plugin.getConfig().getString("messages.join.title.subtitle"));
            int fadeIn = plugin.getConfig().getInt("messages.join.title.fadeIn");
            int stay = plugin.getConfig().getInt("messages.join.title.stay");
            int fadeOut = plugin.getConfig().getInt("messages.join.title.fadeOut");

            player.showTitle(net.kyori.adventure.title.Title.title(
                    ConfigManager.colorizeComponent(title),
                    ConfigManager.colorizeComponent(subtitle),
                    net.kyori.adventure.title.Title.Times.times(
                            java.time.Duration.ofMillis(fadeIn * 50L),
                            java.time.Duration.ofMillis(stay * 50L),
                            java.time.Duration.ofMillis(fadeOut * 50L)
                    )
            ));
        }

        // Play sound if enabled
        if (plugin.getConfig().getBoolean("sounds.join.enabled")) {
            try {
                Sound sound = Sound.valueOf(plugin.getConfig().getString("sounds.join.sound"));
                float volume = (float) plugin.getConfig().getDouble("sounds.join.volume");
                float pitch = (float) plugin.getConfig().getDouble("sounds.join.pitch");
                player.playSound(player.getLocation(), sound, volume, pitch);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid sound name in config!");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Check if leave messages are enabled
        if (!plugin.getConfig().getBoolean("messages.leave.enabled")) {
            event.quitMessage(null);
            return;
        }

        String format = plugin.getConfig().getString("messages.leave.format");
        String message = processPlaceholders(player, format);

        // Set quit message using Component
        if (plugin.getConfig().getBoolean("messages.leave.broadcast")) {
            event.quitMessage(ConfigManager.colorizeComponent(message));
        } else {
            event.quitMessage(null);
            player.sendMessage(ConfigManager.colorizeComponent(message));
        }

        // Play sound if enabled
        if (plugin.getConfig().getBoolean("sounds.leave.enabled")) {
            try {
                Sound sound = Sound.valueOf(plugin.getConfig().getString("sounds.leave.sound"));
                float volume = (float) plugin.getConfig().getDouble("sounds.leave.volume");
                float pitch = (float) plugin.getConfig().getDouble("sounds.leave.pitch");
                player.playSound(player.getLocation(), sound, volume, pitch);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid sound name in config!");
            }
        }
    }

    private String processPlaceholders(Player player, String text) {
        if (text == null) return "";

        String processed = text;

        // Custom player name placeholder
        if (plugin.getConfig().getBoolean("custom-placeholders.enabled")) {
            String customNameFormat = plugin.getConfig().getString("custom-placeholders.player-name-format");
            if (customNameFormat != null) {
                String customName = customNameFormat.replace("%player_name%", player.getName());
                processed = processed.replace("%player_name%", ConfigManager.colorize(customName));
            }
        } else {
            processed = processed.replace("%player_name%", player.getName());
        }

        // Other basic placeholders
        processed = processed.replace("%player_displayname%", player.getDisplayName());
        processed = processed.replace("%world%", player.getWorld().getName());

        // PlaceholderAPI placeholders
        if (plugin.hasPlaceholderAPI()) {
            processed = PlaceholderAPI.setPlaceholders(player, processed);
        }

        return processed;
    }
}