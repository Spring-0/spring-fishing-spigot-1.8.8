package dev.spring93.springfishing.services;

import de.tr7zw.nbtapi.NBTItem;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.items.ItemUtils;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FishingRodService {
    private ConfigService config;

    public FishingRodService() {
        config = ConfigService.getInstance();
    }

    public FishingRod getFishingRod(ItemStack itemStack) {
        NBTItem nbti = new NBTItem(itemStack);
        if (isFishingRod(itemStack)) {
            return new FishingRod(nbti);
        }
        return null;
    }

    public void givePlayerNewFishingRod(CommandSender sender, Player targetPlayer) {
        targetPlayer.getInventory().addItem(ItemUtils.createFishingRodItem());
        MessageUtils.sendMessage(sender, String.format("Successfully gave x1 fishing rod to '%s'", targetPlayer.getName()));
    }

    public void broadcastMaxLevelIfApplicable(FishingRod rod, Player player) {
        String broadcastMessage = config.getMaxedRodBroadcastMessage().replace("%player%", player.getDisplayName());
        if(rod.isMaxed() && !broadcastMessage.isEmpty()) {
            MessageUtils.broadcastMessage(broadcastMessage);
        }
    }

    public boolean isFishingRod(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasTag("isFishingRod");
    }
}
