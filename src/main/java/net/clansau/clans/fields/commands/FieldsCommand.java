package net.clansau.clans.fields.commands;

import net.clansau.clans.fields.FieldsManager;
import net.clansau.clans.fields.FieldsModule;
import net.clansau.core.client.Rank;
import net.clansau.core.framework.command.Command;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class FieldsCommand extends Command<FieldsManager, CommandSender> {

    public FieldsCommand(final FieldsManager manager) {
        super(manager, CommandSender.class, "fields", new String[0]);
    }

    @Override
    public Rank getDefaultRequiredRank() {
        return Rank.ADMIN;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args == null || args.length == 0) {
            help(sender);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "reset":
            case "replenish":
                this.resetCommand(sender);
                break;
            case "info":
            case "status":
                this.infoCommand(sender);
                break;
            default:
                help(sender);
                break;
        }
    }

    @Override
    protected void help(final CommandSender sender) {
        UtilMessage.message(sender, "Fields", "Commands List:");
        UtilMessage.messageCMD(sender, "/fields reset", "Replenish the Fields.");
        UtilMessage.messageCMD(sender, "/fields info", "Show Fields Information.");
    }

    private void resetCommand(final CommandSender sender) {
        if (getManager().getGameBlocks().size() <= 0) {
            UtilMessage.message(sender, "Fields", "There are currently no blocks to replenish.");
            return;
        }
        getManager().restore(true);
    }

    private void infoCommand(final CommandSender sender) {
        UtilMessage.message(sender, "Fields", "Current Information:");
        for (final String s : this.getInfo()) {
            UtilMessage.message(sender, s);
        }
    }

    private String[] getInfo() {
        return new String[]{
                ChatColor.GREEN + "Status: " + ChatColor.WHITE + UtilFormat.getStatus(getManager().getModule(FieldsModule.class).isEnabled(), false),
                ChatColor.GREEN + "Blocks: " + ChatColor.WHITE + (getManager().getSavedBlocks().size() - getManager().getGameBlocks().size()),
                ChatColor.GREEN + "Last Replenished: " + ChatColor.WHITE + getManager().getLastReplenished()
        };
    }
}