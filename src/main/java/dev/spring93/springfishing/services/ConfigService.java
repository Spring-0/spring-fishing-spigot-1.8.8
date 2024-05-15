package dev.spring93.springfishing.services;

import dev.spring93.springfishing.SpringFishing;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigService {

    private static ConfigService instance;
    private static FileConfiguration config;

    public ConfigService() {
        SpringFishing plugin = JavaPlugin.getPlugin(SpringFishing.class);
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public static ConfigService getInstance() {
        if(instance == null) {
            instance = new ConfigService();
        }
        return instance;
    }

    public String getInvalidArgsNumberMessage() {
        return getColorCodedString("invalid-argument-amount-message");
    }

    public String getInvalidArgMessage() {
        return getColorCodedString("invalid-argument-message");
    }

    public String getNoPermissionMessage() {
        return getColorCodedString("no-permission-message");
    }

    public String getMessagePrefix() {
        return getColorCodedString("message-prefix");
    }

    public String getColorCodedString(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    public String getColorCodedString(ConfigurationSection configSection, String path) {
        return ChatColor.translateAlternateColorCodes('&', configSection.getString(path));
    }

    public void reloadConfig(CommandSender sender) {
        SpringFishing plugin = SpringFishing.getInstance();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        MessageUtils.sendMessage(sender, "has been reloaded.");
    }

    public String getFishingRodDisplayName() {
        return getColorCodedString("fishing-rod-item.display-name");
    }

    public String getMaxedFishingRodDisplayName() {
        return getColorCodedString("maxed-fishing-rod-item.display-name");
    }

    public List<String> getColoredStringList(ConfigurationSection configSection, String path) {
        List<String> lore = configSection.getStringList(path);
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line);
            lore.set(i, line);
        }
        return lore;
    }

    public List<String> getColoredStringList(String path) {
        return getColoredStringList(config, path);
    }

    public List<String> getFishingRodLore() {
        return getColoredStringList("fishing-rod-item.lore");
    }

    public List<String> getMaxedFishingRodLore() {
        return getColoredStringList("maxed-fishing-rod-item.lore");
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
        return getColorCodedString("player-maxed-fishing-rod-broadcast");
    }

    public boolean isVanillaExpEnabled() {
        return !config.getBoolean("fishing-rewards.disable-vanilla-exp");
    }

    public List<ConfigurationSection> getFishingRewardsConfigList() {
        List<ConfigurationSection> rewardsSections = new ArrayList<>();
        ConfigurationSection fishingRewardsSection = config.getConfigurationSection("fishing-rewards.rewards");
        if(fishingRewardsSection != null) {
            Set<String> rewards = fishingRewardsSection.getKeys(false);
            if(rewards != null) {
                for(String rewardKey : rewards) {
                    ConfigurationSection rewardSection = fishingRewardsSection.getConfigurationSection(rewardKey);
                    if(rewardSection != null) {
                        rewardsSections.add(rewardSection);
                    }
                }
            }
        }
        return rewardsSections;
    }

    public String getRodLevelUpMessage() {
        return getColorCodedString("fishing-rod-level-up");
    }
}
