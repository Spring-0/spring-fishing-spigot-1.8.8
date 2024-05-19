package dev.spring93.springfishing;

import dev.spring93.springfishing.commands.SpringFishingCommands;
import dev.spring93.springfishing.listeners.FishingEventListener;
import dev.spring93.springfishing.listeners.PlayerDeathListener;
import dev.spring93.springfishing.services.EnchantService;
import dev.spring93.springfishing.services.FishingRewardService;
import dev.spring93.springfishing.services.FishingRodService;
import dev.spring93.springfishing.services.FishingService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpringFishing extends JavaPlugin {
    private static SpringFishing instance;
    private static Economy econ = null;
    private FishingRodService rodService;
    private FishingRewardService fishingRewardService;
    private FishingService fishingService;
    private EnchantService enchantService;

    @Override
    public void onEnable() {
        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("[SpringFishing] - Disabled due to no Vault dependency found!")                              ;
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.rodService = new FishingRodService();
        this.fishingRewardService = new FishingRewardService();
        this.fishingService =  new FishingService();
        this.enchantService = new EnchantService(fishingRewardService);

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
        this.getServer().getPluginManager().registerEvents(new FishingEventListener(
                rodService,
                fishingRewardService,
                fishingService,
                enchantService), this);

        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(
                enchantService,
                rodService), this);
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
