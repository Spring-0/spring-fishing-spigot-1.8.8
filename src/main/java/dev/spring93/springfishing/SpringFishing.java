package dev.spring93.springfishing;

import dev.spring93.springfishing.commands.SpringFishingCommands;
import dev.spring93.springfishing.listeners.FishingEventListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpringFishing extends JavaPlugin {
    private static SpringFishing instance;
    private static Economy econ = null;

    @Override
    public void onEnable() {
        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("[SpringFishing] - Disabled due to no Vault dependency found!")                              ;
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerListeners();

        getLogger().info("[SpringFishing] successfully enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("[SpringFishing] has been disabled.");
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
