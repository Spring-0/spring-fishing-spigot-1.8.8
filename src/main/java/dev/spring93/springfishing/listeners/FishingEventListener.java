package dev.spring93.springfishing.listeners;
import dev.spring93.springfishing.items.FishingReward;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.services.FishingRewardService;
import dev.spring93.springfishing.services.FishingRodService;
import dev.spring93.springfishing.services.ConfigService;
import dev.spring93.springfishing.utils.MessageUtils;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.lang.reflect.Field;

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
        Player player = event.getPlayer();
        FishingRod rod = rodService.getFishingRod(player.getItemInHand());
        if(rod == null) return;

        if(event.getState() == PlayerFishEvent.State.FISHING) {
            int calculatedBiteTime = config.getBaseBiteTime() - (config.getBaseTimeReduction() * (rod.getLevel() - 1));
            if(calculatedBiteTime < 1) calculatedBiteTime = 1;
            setBiteTime(event.getHook(), calculatedBiteTime);
        } else if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
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

    private void setBiteTime(FishHook hook, int timeSeconds) {
        int timeInTicks = timeSeconds * 20;
        EntityFishingHook hookCopy = (EntityFishingHook) ((CraftEntity) hook).getHandle();

        Field fishCatchTime = null;
        try {
            fishCatchTime = EntityFishingHook.class.getDeclaredField("aw");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        fishCatchTime.setAccessible(true);

        try {
            fishCatchTime.setInt(hookCopy, timeInTicks);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        fishCatchTime.setAccessible(false);
    }

}
