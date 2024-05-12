package dev.spring93.springfishing.utils;

import dev.spring93.springfishing.SpringFishing;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigManager {

    private static ConfigManager instance;
    private static FileConfiguration config;

    public ConfigManager() {
        SpringFishing plugin = JavaPlugin.getPlugin(SpringFishing.class);
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public static ConfigManager getInstance() {
        if(instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getInvalidArgsNumberMessage() {
        return getConfigString("invalid-argument-amount-message");
    }

    public String getInvalidArgMessage() {
        return getConfigString("invalid-argument-message");
    }

    public String getNoPermissionMessage() {
        return getConfigString("no-permission-message");
    }

    public String getMessagePrefix() {
        return getConfigString("message-prefix");
    }

    private String getConfigString(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    public void reloadConfig(CommandSender sender) {
        SpringFishing plugin = SpringFishing.getInstance();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        MessageManager.sendMessage(sender, "has been reloaded.");
    }

    public String getFishingRodDisplayName() {
        return getConfigString("fishing-rod-item.display-name");
    }

    public String getMaxedFishingRodDisplayName() {
        return getConfigString("maxed-fishing-rod-item.display-name");
    }

    public List<String> getFishingRodLore() {
        List<String> lore = config.getStringList("fishing-rod-item.lore");
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line);
            lore.set(i, line);
        }
        return lore;
    }

    public List<String> getMaxedFishingRodLore() {
        List<String> lore = config.getStringList("maxed-fishing-rod-item.lore");
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line);
            lore.set(i, line);
        }
        return lore;
    }

    public boolean isFishingRodGlow() {
        return config.getBoolean("fishing-rod-item.glow");
    }

    public int getBaseFishFrequency() {
        return config.getInt("fishing-rod-upgrades.base-fish-frequency");
    }

    public int getFishRequiredToUpgrade(String level) {
        int requiredToUpgrade = config.getInt("fishing-rod-upgrades.levels." + level + ".fish-required-to-upgrade");
        return requiredToUpgrade;
    }

    public boolean isLifeBoundEnabled(String level) {
        return config.getBoolean("fishing-rod-upgrades.levels." + level + ".life-bound.enabled");
    }

    public double getLifeBoundSuccessChance(String level) {
        return config.getDouble("fishing-rod-upgrades.levels." + level + ".life-bound.success-chance");
    }

    public boolean isDoubleOrNothingEnabled(String level) {
        return config.getBoolean("fishing-rod-upgrades.levels." + level + ".double-or-nothing.enabled");
    }

    public double getDoubleRate(String level) {
        return config.getDouble("fishing-rod-upgrades.levels." + level + ".double-or-nothing.double-rate");
    }

    public double getNothingRate(String level) {
        return config.getDouble("fishing-rod-upgrades.levels." + level + ".double-or-nothing.nothing-rate");
    }

    public boolean isFishFrenzyEnabled(String level) {
        return config.getBoolean("fishing-rod-upgrades.levels." + level + ".fish-frenzy.enabled");
    }

    public double getFishFrenzyActivationRate(String level) {
        return config.getDouble("fishing-rod-upgrades.levels." + level + ".fish-frenzy.activation-rate");
    }

    public int getFishFrenzyDuration(String level) {
        return config.getInt("fishing-rod-upgrades.levels." + level + ".fish-frenzy.duration");
    }

    public boolean isLevelExists(String level) {
        return config.contains("fishing-rod-upgrades.levels." + level);
    }

    public String getMaxedRodBroadcastMessage() {
        return config.getString("player-maxed-fishing-rod-broadcast");
    }
}
