package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.chat.AllyChatCommand;
import me.trae.clans.clan.commands.chat.ClanChatCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import org.bukkit.entity.Player;

public class HelpCommand extends ClanSubCommand {

    public HelpCommand(final ClanCommand module) {
        super(module, "help");
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        this.getModule().help(player);

        this.getModule().getManager().getModuleByClass(ClanChatCommand.class).sendUsageMessage(player);
        this.getModule().getManager().getModuleByClass(AllyChatCommand.class).sendUsageMessage(player);
    }
}