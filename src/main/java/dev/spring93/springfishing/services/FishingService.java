package dev.spring93.springfishing.services;

import net.minecraft.server.v1_8_R3.EntityFishingHook;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.FishHook;

import java.lang.reflect.Field;

public class FishingService {
    public FishingService() {}

    public void setBiteTime(FishHook hook, int timeSeconds) {
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
