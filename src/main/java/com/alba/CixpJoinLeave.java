package com.alba;

import com.alba.listeners.JoinLeaveListener;
import com.alba.placeholders.CustomPlaceholderExpansion;
import com.alba.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CixpJoinLeave extends JavaPlugin {

    private static CixpJoinLeave instance;
    private ConfigManager configManager;
    private boolean placeholderAPI = false;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config
        saveDefaultConfig();

        // Initialize ConfigManager
        configManager = new ConfigManager(this);

        // Check for PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPI = true;
            getLogger().info("PlaceholderAPI found! Hooked successfully.");

            // Register custom placeholders
            if (getConfig().getBoolean("custom-placeholders.enabled")) {
                new CustomPlaceholderExpansion(this).register();
                getLogger().info("Custom placeholders registered!");
            }
        } else {
            getLogger().warning("PlaceholderAPI not found! Some features may not work.");
        }

        // Register listener
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);

        getLogger().info("CixpJoinLeave has been enabled!");
        getLogger().info("Created by cixp");
    }

    @Override
    public void onDisable() {
        getLogger().info("CixpJoinLeave has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("cixpjoinleave")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("cixpjoinleave.reload")) {
                    reloadConfig();
                    configManager.reload();

                    // Re-register placeholders if needed
                    if (placeholderAPI && getConfig().getBoolean("custom-placeholders.enabled")) {
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            new CustomPlaceholderExpansion(this).register();
                        }
                    }

                    sender.sendMessage("§aCixpJoinLeave configuration reloaded!");
                } else {
                    sender.sendMessage("§cYou don't have permission to do this!");
                }
                return true;
            }
        }
        return false;
    }

    public static CixpJoinLeave getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public boolean hasPlaceholderAPI() {
        return placeholderAPI;
    }
}