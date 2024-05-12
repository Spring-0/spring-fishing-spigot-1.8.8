package dev.spring93.springfishing.services;

import de.tr7zw.nbtapi.NBTItem;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.items.ItemUtils;
import dev.spring93.springfishing.utils.ConfigManager;
import dev.spring93.springfishing.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FishingRodService {
    private ConfigManager config;

    public FishingRodService() {
        config = ConfigManager.getInstance();
    }

    public FishingRod getFishingRod(ItemStack itemStack) {
        NBTItem nbti = new NBTItem(itemStack);
        if (nbti.hasKey("isFishingRod")) {
            return new FishingRod(nbti);
        }
        return null;
    }

    public void givePlayerNewFishingRod(CommandSender sender, String targetPlayerName) {
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if(targetPlayer == null) {
            MessageManager.sendMessage(sender, String.format("Player '%s' not found.", targetPlayerName));
            return;
        }

        targetPlayer.getInventory().addItem(ItemUtils.createFishingRodItem());
        MessageManager.sendMessage(sender, String.format("Successfully gave x1 fishing rod to '%s'", targetPlayerName));
    }

    public void broadcastMaxLevelIfApplicable(FishingRod rod, Player player) {
        String broadcastMessage = config.getMaxedRodBroadcastMessage().replace("%player%", player.getDisplayName());
        if(rod.isMaxed() && !broadcastMessage.isEmpty()) {
            MessageManager.broadcastMessage(broadcastMessage);
        }
    }
}
