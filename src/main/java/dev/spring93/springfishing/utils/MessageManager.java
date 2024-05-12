package dev.spring93.springfishing.utils;

import dev.spring93.springfishing.SpringFishing;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class MessageManager {

    private static ConfigManager config = ConfigManager.getInstance();

    public static void broadcastMessage(String msg) {
        Bukkit.broadcastMessage(config.getMessagePrefix() + msg);
    }

    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(config.getMessagePrefix() + msg);
    }

    public static String getHelpMenu() {
        return "\n" + ChatColor.DARK_GRAY + "------" + ChatColor.GREEN + " SpringFishing " + ChatColor.DARK_GRAY + "------\n" +
                "\n" + ChatColor.GREEN + "/fishing help " + ChatColor.YELLOW + ": displays this message\n" +
                ChatColor.GREEN + "/fishing give [player] " + ChatColor.YELLOW + ": gives the specified player a fishing rod.\n" +
                ChatColor.GREEN + "/fishing ver " + ChatColor.YELLOW + ": displays plugin information\n" +
                ChatColor.GREEN + "/fishing reload " + ChatColor.YELLOW + ": reloads plugin configuration\n";
    }

    public static String getVersionMessage() {
        return ChatColor.LIGHT_PURPLE + "Plugin Name " + ChatColor.GREEN + ": SpringFishing" + "\n" +
                ChatColor.LIGHT_PURPLE + "Author " + ChatColor.GREEN + ": Spring-0" + "\n" +
                ChatColor.LIGHT_PURPLE + "GitHub " + ChatColor.GREEN + ": https://github.com/Spring-0/spring-fishing-1.8.8" + "\n" +
                ChatColor.LIGHT_PURPLE + "Spigot " + ChatColor.GREEN + ": https://www.spigotmc.org/" + "\n" +
                ChatColor.LIGHT_PURPLE + "Version " + ChatColor.GREEN + ": " + SpringFishing.getInstance().getDescription().getVersion();
    }
}
