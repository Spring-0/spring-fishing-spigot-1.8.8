package dev.spring93.springfishing.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static boolean isPlayerExistByName(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName.toLowerCase().trim());
        return !(player == null);
    }
}
