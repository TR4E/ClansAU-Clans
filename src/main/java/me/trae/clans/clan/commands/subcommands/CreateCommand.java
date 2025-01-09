package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.command.ClanCreateEvent;
import me.trae.clans.clan.types.AdminClan;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class CreateCommand extends ClanSubCommand implements EventContainer<ClanCreateEvent> {

    @ConfigInject(type = Integer.class, path = "Min-Name-Length", defaultValue = "3")
    private int minNameLength;

    @ConfigInject(type = Integer.class, path = "Max-Name-Length", defaultValue = "14")
    private int maxNameLength;

    public CreateCommand(final ClanCommand module) {
        super(module, "create");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <name>";
    }

    @Override
    public String getDescription() {
        return "Create a Clan";
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, Clan clan, final String[] args) {
        if (clan != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan.");
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Clans", "You did not input a Name to Create.");
            return;
        }

        final String name = args[0];

        if (!(this.canCreateClan(player, client, name))) {
            return;
        }

        if (client.isAdministrating() && client.hasRank(Rank.OWNER)) {
            clan = new AdminClan(name, player);
        } else {
            clan = new Clan(name, player);
        }

        this.callEvent(new ClanCreateEvent(clan, player, client));
    }

    private boolean canCreateClan(final Player player, final Client client, final String name) {
        if (this.getModule().isSubCommandByLabel(name)) {
            UtilMessage.message(player, "Clans", "You cannot use that as your Clan name!");
            return false;
        }

        if (UtilString.hasSymbols(name) || !(UtilString.hasValidCharacters(name))) {
            UtilMessage.message(player, "Clans", "You cannot have special characters in your Clan name!");
            return false;
        }

        if (this.getModule().getManager().isClanByName(name)) {
            UtilMessage.message(player, "Clans", "Clan name is already used by another Clan!");
            return false;
        }

        final int maxNameLength = this.maxNameLength;
        if (name.length() > maxNameLength) {
            UtilMessage.simpleMessage(player, "Clans", "Clan name is too long! Maximum Length is <yellow><var></yellow>!", Collections.singletonList(String.valueOf(maxNameLength)));
            return false;
        }

        if (!(client.isAdministrating())) {
            final int minNameLength = this.minNameLength;
            if (name.length() < minNameLength) {
                UtilMessage.simpleMessage(player, "Clans", "Clan name is too short! Minimum Length is <yellow><var></yellow>!", Collections.singletonList(String.valueOf(minNameLength)));
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanCreateEvent> getClassOfEvent() {
        return ClanCreateEvent.class;
    }

    @Override
    public void onEvent(final ClanCreateEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        this.getModule().getManager().addClan(clan);
        this.getModule().getManager().getRepository().saveData(clan);

        for (final Player target : UtilServer.getOnlinePlayers()) {
            final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByClan(this.getModule().getManager().getClanByPlayer(target), clan);

            UtilMessage.simpleMessage(target, "Clans", "<var> formed <var>.", Arrays.asList(clanRelation.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(clan, clanRelation)));
        }
    }
}