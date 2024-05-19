package dev.spring93.springfishing.services;

import de.tr7zw.nbtapi.NBTItem;
import dev.spring93.springfishing.SpringFishing;
import dev.spring93.springfishing.utils.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EconService {
    private final Economy econ;
    private final ConfigService config;

    public EconService() {
        this.econ = SpringFishing.getEconomy();
        this.config = ConfigService.getInstance();
    }

    public void sellAllFish(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        double totalSellAmount = 0;
        int itemsSold = 0;
        for(ItemStack item : playerInventory.getContents()) {
            if(item == null) continue;

            NBTItem nbtItem = new NBTItem(item);
            if(nbtItem.hasTag("reward-key")) {
                String key = nbtItem.getString("reward-key");
                double sellValue = config.getSellPriceFromRewardKey(key);

                playerInventory.remove(item);
                totalSellAmount += (sellValue * item.getAmount());
                itemsSold += (1 * item.getAmount());
            }
        }

        if(totalSellAmount < 1) {
            MessageUtils.sendMessage(player, config.getNoFishToSellMessage());
            return;
        }

        if(econ.depositPlayer(player, totalSellAmount).transactionSuccess()) {
            Bukkit.getLogger().info(
                    String.format("Player %s sold %d fishing items for %f", player.getName(), itemsSold, totalSellAmount));

            MessageUtils.sendMessage(player, config.getFishSoldMessage()
                    .replace("%amount%", econ.format(totalSellAmount))
                    .replace("%amount_sold%", String.valueOf(itemsSold))
            );
        }
        else Bukkit.getLogger().severe(
                String.format("[SpringFishing] Error depositing %d to player %s", totalSellAmount, player.getName())
        );
    }

}
