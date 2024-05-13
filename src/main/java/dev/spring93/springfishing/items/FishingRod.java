package dev.spring93.springfishing.items;

import de.tr7zw.nbtapi.NBTItem;
import dev.spring93.springfishing.services.ConfigService;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FishingRod {
    private ConfigService config;
    private NBTItem nbti;

    public FishingRod(NBTItem nbti) {
        this.nbti = nbti;
        config = ConfigService.getInstance();
    }

    public NBTItem getNBTItem() {
        return nbti;
    }

    public void setLevel(int level) {
        nbti.setInteger("level", level);
    }

    public int getLevel() {
        return nbti.getInteger("level");
    }

    public void setFishCaught(int fishCaught) {
        nbti.setInteger("fishCaught", fishCaught);
    }

    public int getFishCaught() {
        return nbti.getInteger("fishCaught");
    }

    public void incrementFishCaught() {
        int fishCaught = getFishCaught();
        setFishCaught(fishCaught + 1);
    }

    public boolean shouldLevelUp(int currentLevel) {
        if(isMaxed()) return false;

        int fishRequired = getFishToUpgrade(currentLevel);
        int fishCaught = getFishCaught();

        return fishCaught >= fishRequired;
    }

    private int getFishToUpgrade(int currentLevel) {
        int fishRequired = 0;

        for(; currentLevel > 0; currentLevel--) {
            fishRequired += config.getFishRequiredToUpgrade(String.valueOf(currentLevel));
        }

        return fishRequired;
    }

    public void updateMeta() {
        ItemMeta meta = nbti.getItem().getItemMeta();

        if(meta != null) {
            List<String> lore;
            String displayName;

            if(isMaxed()) {
                lore = config.getMaxedFishingRodLore();
                displayName = config.getMaxedFishingRodDisplayName();
            }
            else {
                lore = config.getFishingRodLore();
                displayName = config.getFishingRodDisplayName();
            }

            for(int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                line = line.replace("%current_level%", String.valueOf(getLevel()));
                line = line.replace("%fish_left%", String.valueOf(calculateFishLeft()));
                line = line.replace("%fish_caught%", String.valueOf(getFishCaught()));
                lore.set(i, line);
            }
            meta.setLore(lore);
            meta.setDisplayName(displayName);

            nbti.getItem().setItemMeta(meta);
        }
    }

    private int calculateFishLeft() {
        return getFishToUpgrade(getLevel()) - getFishCaught();
    }

    public boolean isMaxed() {
        return !config.isLevelExists(String.valueOf(getLevel() + 1));
    }

}
