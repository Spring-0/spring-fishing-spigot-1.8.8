package dev.spring93.springfishing.services;

import dev.spring93.springfishing.items.FishingReward;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

public class EnchantService {
    private ConfigService config;
    private FishingRewardService rewardService;

    public EnchantService(FishingRewardService rewardService) {
        this.config = ConfigService.getInstance();
        this.rewardService = rewardService;
    }

    public boolean handleDoubleOrNothing(PlayerFishEvent event, FishingRod rod) {
        int rodLevel = rod.getLevel();
        if(!config.isDoubleOrNothingEnabled(rodLevel)) return true;

        Player player = event.getPlayer();
        double activationRate = config.getDoubleOrNothingActivationRate(rodLevel);
        double random = Math.random();

        if (random < activationRate) {
            FishingReward reward = rewardService.getRandomReward();

            if (!reward.isCommand()) player.getInventory().addItem(reward.getItemStack());
            else Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    reward.getCommand().replace("%player%", player.getName()));
            MessageUtils.sendMessage(player, config.getFishDoubledMessage());
            return true;
        } else {
            MessageUtils.sendMessage(player, config.getNothingFishMessage());
            event.getCaught().remove();
            return false;
        }
    }

    public boolean isLifeBoundProc(FishingRod rod) {
        int rodLevel = rod.getLevel();
        return Math.random() < config.getLifeBoundActivationRate(rodLevel);
    }
}

