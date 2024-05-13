package dev.spring93.springfishing.commands;

import dev.spring93.springfishing.services.FishingRodService;
import dev.spring93.springfishing.services.ConfigService;
import dev.spring93.springfishing.utils.MessageUtils;
import org.bukkit.command.CommandSender;

public class SpringFishingCommands extends BaseCommand {

    private FishingRodService fishingRodService;
    private ConfigService config;

    public SpringFishingCommands() {
        super("fishing");
        fishingRodService = new FishingRodService();
        config = ConfigService.getInstance();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        String arg1 = args[0].toLowerCase().trim();

        switch(arg1) {
            case "give":
                if(!sender.hasPermission("springfishing.command.give")) {
                    MessageUtils.sendMessage(sender, config.getNoPermissionMessage());
                    break;
                }

                if(args.length == 2) {
                    String targetPlayer = args[1].toLowerCase().trim();
                    fishingRodService.givePlayerNewFishingRod(sender, targetPlayer);
                } else MessageUtils.sendMessage(sender, ConfigService.getInstance().getInvalidArgsNumberMessage());
                break;
            case "reload":
                if(!sender.hasPermission("springfishing.command.reload")) {
                    MessageUtils.sendMessage(sender, config.getNoPermissionMessage());
                    break;
                }
                config.reloadConfig(sender);
                break;
            case "ver":
                if(!sender.hasPermission("springfishing.command.version")) {
                    MessageUtils.sendMessage(sender, config.getNoPermissionMessage());
                    break;
                }
                MessageUtils.sendMessage(sender, MessageUtils.getVersionMessage());
                break;
            default:
                sender.sendMessage(MessageUtils.getHelpMenu());
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
