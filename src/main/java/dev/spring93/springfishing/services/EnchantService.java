package dev.spring93.springfishing.services;

import dev.spring93.springfishing.SpringFishing;
import dev.spring93.springfishing.items.FishingReward;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EnchantService {
    private ConfigService config;
    private FishingRewardService rewardService;
    private FishingRodService rodService;

    public EnchantService(FishingRewardService rewardService, FishingRodService rodService) {
        this.config = ConfigService.getInstance();
        this.rewardService = rewardService;
        this.rodService = rodService;
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

    public int handleFishingFrenzy(FishingRod rod, Player player) {
        int rodLevel = rod.getLevel();
        int calculatedBiteTime = -1;
        if(config.isFishFrenzyEnabled(rodLevel)) {
            if(rodService.isFrenzyActive(player.getUniqueId())) {
                calculatedBiteTime = config.getFishFrenzyBiteTime(rodLevel);
                if(calculatedBiteTime < 1) calculatedBiteTime = 1;
            } else {
                if(Math.random() < config.getFishFrenzyActivationRate(rodLevel)) {
                    int duration = config.getFishFrenzyDuration(rodLevel);
                    rodService.setFrenzyActive(player.getUniqueId(), true);
                    MessageUtils.sendMessage(player, config.getFrenzyActivatedMessage(duration));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            rodService.setFrenzyActive(player.getUniqueId(), false);
                            MessageUtils.sendMessage(player, config.getFrenzyEndedMessage());
                        }
                    }.runTaskLater(SpringFishing.getInstance(), duration * 20);
                }
            }
        }
        return calculatedBiteTime;
    }

}

