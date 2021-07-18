package net.clansau.clans.fishing.commands;

import net.clansau.clans.fishing.Fish;
import net.clansau.clans.fishing.FishingManager;
import net.clansau.clans.fishing.enums.TopRating;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.client.Rank;
import net.clansau.core.framework.command.Command;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class FishCommand extends Command<FishingManager, CommandSender> {

    public FishCommand(final FishingManager manager) {
        super(manager, CommandSender.class, "fish", new String[]{"fishing"});
    }

    @Override
    public Rank getDefaultRequiredRank() {
        return Rank.PLAYER;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args == null || args.length == 0) {
            help(sender);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "top": {
                this.topCommand(sender, args);
                break;
            }
            default: {
                help(sender);
                break;
            }
        }
    }

    @Override
    protected void help(final CommandSender sender) {
        UtilMessage.message(sender, "Fishing", "Commands List:");
        UtilMessage.messageCMD(sender, "/fish top <10|day|week>", "View Top Statistics.");
    }

    private void topCommand(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            UtilMessage.message(sender, "Fishing", "Usage: " + ChatColor.AQUA + "/fish top <10|day|week>");
            return;
        }
        TopRating rating = TopRating.TOP_10;
        if (args.length == 2) {
            switch (args[1].toLowerCase()) {
                case "today":
                case "day": {
                    rating = TopRating.TOP_TODAY;
                    break;
                }
                case "week": {
                    rating = TopRating.TOP_WEEK;
                    break;
                }
            }
        }
        UtilMessage.message(sender, ChatColor.AQUA.toString() + ChatColor.BOLD + "Showing " + UtilFormat.cleanString(rating.name()) + ":");
        int count = 0;
        for (final Fish fish : getManager().getTop(rating)) {
            final Client client = getInstance().getManager(ClientManager.class).tryGetOnlineClient(fish.getUUID());
            if (client == null) {
                continue;
            }
            count++;
            UtilMessage.message(sender, ChatColor.RED.toString() + ChatColor.BOLD + "#" + count + ": " + ChatColor.YELLOW + client.getName() + ChatColor.GRAY + " - " + ChatColor.LIGHT_PURPLE + fish.getName() + ChatColor.GRAY + " (" + ChatColor.GREEN + fish.getSize() + " Pound" + ChatColor.GRAY + ")");
        }
    }
}