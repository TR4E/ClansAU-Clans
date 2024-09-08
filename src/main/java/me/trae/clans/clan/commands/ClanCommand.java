package me.trae.clans.clan.commands;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.commands.subcommands.CreateCommand;
import me.trae.clans.clan.commands.subcommands.DisbandCommand;
import me.trae.clans.clan.commands.subcommands.HelpCommand;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.utility.UtilCommand;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

public class ClanCommand extends Command<Clans, ClanManager> implements PlayerCommandType {

    public ClanCommand(final ClanManager manager) {
        super(manager, "clan", new String[]{"c", "faction", "fac", "f", "gang", "g"}, Rank.DEFAULT);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new CreateCommand(this));
        addSubModule(new DisbandCommand(this));
        addSubModule(new HelpCommand(this));
    }

    @Override
    public String getDescription() {
        return "Clan management";
    }

    @Override
    public String getHelpPrefix() {
        return "Clans";
    }

    @Override
    public void execute(final Player player, final Client client, final String[] args) {
        if (args.length > 0 && this.isSubCommandByLabel(args[0])) {
            UtilCommand.processSubCommand(this, player, args);
            return;
        }

        final Clan playerClan = this.getManager().getClanByPlayer(player);
        Clan targetClan = null;

        if (args.length == 0) {
            if (playerClan == null) {
                UtilMessage.message(player, "Clans", "You are not in a Clan.");
                return;
            }

            targetClan = playerClan;
        }

        if (args.length == 1) {
            targetClan = this.getManager().searchClan(player, args[0], true);
        }

        if (targetClan == null) {
            return;
        }

        UtilMessage.simpleMessage(player, "Clans", "<var> Information:", Collections.singletonList(this.getManager().getClanShortName(targetClan, this.getManager().getClanRelationByClan(playerClan, targetClan))));

        for (final Map.Entry<String, String> entry : this.getManager().getClanInformation(player, client, playerClan, targetClan).entrySet()) {
            UtilMessage.simpleMessage(player, UtilString.pair(entry.getKey(), entry.getValue()));
        }
    }
}