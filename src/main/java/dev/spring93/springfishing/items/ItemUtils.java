package dev.spring93.springfishing.items;

import de.tr7zw.nbtapi.NBTItem;
import dev.spring93.springfishing.services.ConfigService;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtils {

    public static ItemStack createFishingRodItem() {
        ConfigService config = ConfigService.getInstance();

        String displayName = config.getFishingRodDisplayName();
        List<String> lore = config.getFishingRodLore();

        for(int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = line.replace("%current_level%", "1");
            line = line.replace("%fish_left%", String.valueOf(config.getFishRequiredToUpgrade("1")));
            line = line.replace("%fish_caught%", "0");
            lore.set(i, line);
        }

        boolean glow = config.isFishingRodGlow();

        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = rod.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);

            if (glow) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            meta.spigot().setUnbreakable(true);
            rod.setItemMeta(meta);
        }

        NBTItem nbti = new NBTItem(rod);
        nbti.setBoolean("isFishingRod", true);
        nbti.setInteger("level", 1);
        nbti.setInteger("fishCaught", 0);
        return nbti.getItem();
    }
}
