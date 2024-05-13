package dev.spring93.springfishing.listeners;
import dev.spring93.springfishing.items.FishingReward;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.services.FishingRewardService;
import dev.spring93.springfishing.services.FishingRodService;
import dev.spring93.springfishing.services.ConfigService;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingEventListener implements Listener {
    private FishingRodService rodService;
    private FishingRewardService fishingRewardService;
    private ConfigService config;

    public FishingEventListener() {
        this.rodService = new FishingRodService();
        this.fishingRewardService = new FishingRewardService();
        this.config = ConfigService.getInstance();
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            FishingRod rod = rodService.getFishingRod(player.getItemInHand());
            if(rod != null) {
                if(!config.isVanillaExpEnabled()) event.setExpToDrop(0);

                FishingReward reward = fishingRewardService.getRandomReward();

                if(!reward.isCommand()) {
                    ((Item) event.getCaught()).setItemStack(reward.getItemStack());
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward.getCommand().replace("%player%", player.getName()));
                    event.getCaught().remove();
                }

                rod.incrementFishCaught();
                int currentLevel = rod.getLevel();
                if(rod.shouldLevelUp(currentLevel)) {
                    rod.setLevel(currentLevel + 1);
                    MessageUtils.sendMessage(player, config.getRodLevelUpMessage()
                            .replace("%level%", String.valueOf(currentLevel + 1)));

                    rodService.broadcastMaxLevelIfApplicable(rod, player);
                }
                rod.updateMeta();
                player.setItemInHand(rod.getNBTItem().getItem());
            }
        }
    }
}
