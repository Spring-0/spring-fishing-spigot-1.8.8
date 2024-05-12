package dev.spring93.springfishing.commands;

import dev.spring93.springfishing.services.FishingRodService;
import dev.spring93.springfishing.utils.ConfigManager;
import dev.spring93.springfishing.utils.MessageManager;
import org.bukkit.command.CommandSender;

public class SpringFishingCommands extends BaseCommand {

    private FishingRodService fishingRodService;
    private ConfigManager config;

    public SpringFishingCommands() {
        super("fishing");
        fishingRodService = new FishingRodService();
        config = ConfigManager.getInstance();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        String arg1 = args[0].toLowerCase().trim();

        switch(arg1) {
            case "give":
                if(!sender.hasPermission("springfishing.command.give")) {
                    MessageManager.sendMessage(sender, config.getNoPermissionMessage());
                    break;
                }

                if(args.length == 2) {
                    String targetPlayer = args[1].toLowerCase().trim();
                    fishingRodService.givePlayerNewFishingRod(sender, targetPlayer);
                } else MessageManager.sendMessage(sender, ConfigManager.getInstance().getInvalidArgsNumberMessage());
                break;
            case "reload":
                if(!sender.hasPermission("springfishing.command.reload")) {
                    MessageManager.sendMessage(sender, config.getNoPermissionMessage());
                    break;
                }
                config.reloadConfig(sender);
                break;
            case "ver":
                if(!sender.hasPermission("springfishing.command.version")) {
                    MessageManager.sendMessage(sender, config.getNoPermissionMessage());
                    break;
                }
                MessageManager.sendMessage(sender, MessageManager.getVersionMessage());
                break;
            default:
                sender.sendMessage(MessageManager.getHelpMenu());
                break;
        }
        return false;
    }

    @Override
    protected int getMinArgs() {
        return 1;
    }

    @Override
    protected int getMaxArgs() {
        return 2;
    }
}
