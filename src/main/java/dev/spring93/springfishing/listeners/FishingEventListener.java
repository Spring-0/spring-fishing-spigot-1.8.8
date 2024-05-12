package dev.spring93.springfishing.listeners;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.services.FishingRodService;
import dev.spring93.springfishing.utils.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingEventListener implements Listener {
    private FishingRodService rodService;

    public FishingEventListener() {
        this.rodService = new FishingRodService();
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            FishingRod rod = rodService.getFishingRod(player.getItemInHand());
            if(rod != null) {
                rod.incrementFishCaught();
                int currentLevel = rod.getLevel();
                if(rod.shouldLevelUp(currentLevel)) {
                    rod.setLevel(currentLevel + 1);
                    MessageManager.sendMessage(player, "Your fishing rod has leveled up to level: " + (currentLevel + 1));
                    rodService.broadcastMaxLevelIfApplicable(rod, player);
                }
                rod.updateMeta();
                player.setItemInHand(rod.getNBTItem().getItem());
            }
        }
    }
}
