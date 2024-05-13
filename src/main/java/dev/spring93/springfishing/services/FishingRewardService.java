package dev.spring93.springfishing.services;

import dev.spring93.springfishing.items.FishingReward;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishingRewardService {
    private FishingRod rod;
    private List<FishingReward> rewards;
    private final Random random;

    public FishingRewardService() {
        this.random = new Random();
        this.rewards = new ArrayList<>();
        loadRewardsFromConfig(ConfigService.getInstance().getFishingRewardsConfigList());
    }

    private void loadRewardsFromConfig(List<ConfigurationSection> rewardsConfig) {
        if(rewardsConfig != null) {
            for(ConfigurationSection rewardConfig : rewardsConfig) {
                FishingReward fishingReward = new FishingReward(rewardConfig);
                this.rewards.add(fishingReward);
            }
        } else {
            MessageUtils.broadcastMessage("Rewards Config is null");
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

    public void setRod(FishingRod rod) { this.rod = rod; }
}
