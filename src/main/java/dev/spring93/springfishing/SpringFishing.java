package dev.spring93.springfishing;

import dev.spring93.springfishing.commands.SpringFishingCommands;
import dev.spring93.springfishing.listeners.FishingEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpringFishing extends JavaPlugin {
    private static SpringFishing instance;

    @Override
    public void onEnable() {
        instance = this;
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        this.getCommand("fishing").setExecutor(new SpringFishingCommands());
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new FishingEventListener(), this);
    }

    public static SpringFishing getInstance() {
        return instance;
    }
}
