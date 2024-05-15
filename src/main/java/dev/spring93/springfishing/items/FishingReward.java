package dev.spring93.springfishing.items;

import de.tr7zw.nbtapi.NBTItem;
import dev.spring93.springfishing.services.ConfigService;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FishingReward {
    private ItemStack itemStack;
    private final double weight;
    private int rodLevelRequired;
    private double chanceScaleFactorPerLevel;
    private double sellPrice = -1;
    private double sellPriceScaleFactorPerLevel = -1;
    private int itemAmount = -1;
    private String command = null;
    private boolean isCommand = false;
    private ConfigService configService;

    public FishingReward(ConfigurationSection config) {
        this.configService = ConfigService.getInstance();

        if (!config.isSet("command")) {
            this.itemStack = createItemStackFromConfig(config);
            this.sellPrice = config.getDouble("sell-price");
            this.itemAmount = config.getInt("amount");
            this.sellPriceScaleFactorPerLevel = config.getDouble("sell-price-scale-factor-per-rod-level");

        } else {
            this.command = config.getString("command");
            this.isCommand = true;
        }

        this.weight = config.getDouble("base-chance");
        this.rodLevelRequired = config.getInt("min-required-rod-level");
        this.chanceScaleFactorPerLevel = config.getDouble("chance-scale-factor-per-rod-level");
    }

    private ItemStack createItemStackFromConfig(ConfigurationSection itemStackConfigSection) {
        Material material = Material.getMaterial(itemStackConfigSection.getString("display.material-type"));
        String displayName = configService.getColorCodedString(itemStackConfigSection, "display.display-name");
        boolean glow = itemStackConfigSection.getBoolean("display.glow");
        List<String> lore = configService.getColoredStringList(itemStackConfigSection, "display.lore");

        ItemStack newItem = new ItemStack(material);
        ItemMeta meta = newItem.getItemMeta();

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        if(glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        newItem.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(newItem);
        nbtItem.setString("reward-key", itemStackConfigSection.getName());

        return nbtItem.getItem();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getCommand() {
        return command;
    }

    public double getWeight() {
        return weight;
    }

    public int getRodLevelRequired() {
        return rodLevelRequired;
    }

    public double getChanceScaleFactorPerLevel() {
        return chanceScaleFactorPerLevel;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public double getSellPriceScaleFactorPerLevel() {
        return sellPriceScaleFactorPerLevel;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public boolean isCommand() {
        return isCommand;
    }
}
