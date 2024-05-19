package dev.spring93.springfishing.listeners;

import de.tr7zw.nbtapi.NBTItem;
import dev.spring93.springfishing.items.FishingRod;
import dev.spring93.springfishing.services.ConfigService;
import dev.spring93.springfishing.services.EnchantService;
import dev.spring93.springfishing.services.FishingRodService;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathListener implements Listener {
    private ConfigService config;
    private EnchantService enchantService;
    private FishingRodService rodService;
    private final Map<UUID, ItemStack> itemsToKeep = new HashMap<>();

    public PlayerDeathListener(EnchantService enchantService, FishingRodService rodService) {
        config = ConfigService.getInstance();
        this.rodService = rodService;
        this.enchantService = enchantService;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ItemStack[] contents = player.getInventory().getContents();

        for (ItemStack item : contents) {
            if (item != null && new NBTItem(item).getBoolean("isFishingRod")) {
                FishingRod rod = rodService.getFishingRod(item);
                if(!config.isLifeBoundEnabled(rod.getLevel())) return;
                if(!enchantService.isLifeBoundProc(rod)) {
                    MessageUtils.sendMessage(player, config.getLifeBoundFailedMessage());
                    return;
                }
                event.getDrops().remove(item);
                itemsToKeep.put(player.getUniqueId(), item);
                MessageUtils.sendMessage(player, config.getLifeBoundSuccessMessage());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (itemsToKeep.containsKey(playerUUID)) {
            ItemStack itemToRestore = itemsToKeep.get(playerUUID);
            player.getInventory().addItem(itemToRestore);
            itemsToKeep.remove(playerUUID);
        }
    }
}
