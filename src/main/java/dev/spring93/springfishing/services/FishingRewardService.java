package dev.spring93.springfishing.services;

import dev.spring93.springfishing.items.FishingReward;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishingRewardService {
    private FishingRod rod;
    private List<FishingReward> rewards;
    private final Random random;
    private final ConfigService config;

    public FishingRewardService() {
        this.random = new Random();
        this.rewards = new ArrayList<>();
        this.config = ConfigService.getInstance();
    }

    public void loadRewardsFromConfig() {
        List<ConfigurationSection> rewardsConfig = config.getFishingRewardsConfigList();
        if(rewardsConfig != null) {
            for(ConfigurationSection rewardConfig : rewardsConfig) {
                FishingReward fishingReward = new FishingReward(rewardConfig);
                this.rewards.add(fishingReward);
            }
        } else {
            Bukkit.getLogger().severe("[SpringFishing] An error has occurred. Rewards Config is null.");
        }
    }

    public FishingReward getRandomReward() {
        double totalWeight = rewards.stream().mapToDouble(FishingReward::getWeight).sum();
        double randomValue = random.nextDouble() * totalWeight;

        double currentWeight = 0;
        for (FishingReward reward : rewards) {
            currentWeight += reward.getWeight();
            if (randomValue <= currentWeight) {
                return reward;
            }
        }
        return null;
    }

    public void giveReward(PlayerFishEvent event, Player player) {
        FishingReward reward = getRandomReward();
        if(!reward.isCommand()) {
            ((Item) event.getCaught()).setItemStack(reward.getItemStack());
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward.getCommand().replace("%player%", player.getName()));
            event.getCaught().remove();
        }
    }
}
