package dev.spring93.springfishing.listeners;
import dev.spring93.springfishing.SpringFishing;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.services.*;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class FishingEventListener implements Listener {
    private FishingRodService rodService;
    private FishingRewardService fishingRewardService;
    private ConfigService config;
    private EnchantService enchantService;
    private FishingService fishingService;

    public FishingEventListener() {
        this.rodService = new FishingRodService();
        this.fishingRewardService = new FishingRewardService();
        this.fishingService =  new FishingService();
        this.enchantService = new EnchantService(fishingService, fishingRewardService);
        this.config = ConfigService.getInstance();

        fishingRewardService.loadRewardsFromConfig();
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishingRod rod = rodService.getFishingRod(player.getItemInHand());
        if(rod == null) return;
        int rodLevel = rod.getLevel();

        if(event.getState() == PlayerFishEvent.State.FISHING) {
            int calculatedBiteTime = config.getBaseBiteTime() - (config.getBaseTimeReduction() * (rodLevel - 1));
            if(calculatedBiteTime < 1) calculatedBiteTime = 1;

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

            MessageUtils.broadcastMessage("Current bite time: " + calculatedBiteTime);
            fishingService.setBiteTime(event.getHook(), calculatedBiteTime);
        } else if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if(rod != null) {
                if(!config.isVanillaExpEnabled()) event.setExpToDrop(0);

                rod.incrementFishCaught();
                if(rod.shouldLevelUp(rodLevel)) {
                    rod.setLevel(rodLevel + 1);
                    MessageUtils.sendMessage(player, config.getRodLevelUpMessage(rodLevel + 1));
                    rodService.broadcastMaxLevelIfApplicable(rod, player);
                }
                rod.updateMeta();
                player.setItemInHand(rod.getNBTItem().getItem());

                if(!enchantService.handleDoubleOrNothing(event, rod)) return;
                fishingRewardService.giveReward(event, player);
            }
        }
    }
}
