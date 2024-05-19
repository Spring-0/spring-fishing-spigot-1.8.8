package dev.spring93.springfishing.listeners;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.services.*;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingEventListener implements Listener {
    private FishingRodService rodService;
    private FishingRewardService fishingRewardService;
    private ConfigService config;
    private EnchantService enchantService;
    private FishingService fishingService;

    public FishingEventListener(FishingRodService rodService,
                                FishingRewardService rewardService,
                                FishingService fishingService,
                                EnchantService enchantService) {

        this.rodService = rodService;
        this.fishingRewardService = rewardService;
        this.fishingService = fishingService;
        this.enchantService = enchantService;

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

            int fishingFrenzyBiteTime = enchantService.handleFishingFrenzy(rod, player);
            if(fishingFrenzyBiteTime != -1) calculatedBiteTime = fishingFrenzyBiteTime;

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
