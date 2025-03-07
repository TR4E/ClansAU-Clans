package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.enums.RequestType;
import me.trae.clans.clan.events.command.ClanJoinEvent;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.UtilLogger;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Collections;

public class JoinCommand extends ClanSubCommand implements EventContainer<ClanJoinEvent> {

    public JoinCommand(final ClanCommand module) {
        super(module, "join");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Join a Clan";
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, Clan clan, final String[] args) {
        if (clan != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan.");
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Clans", "You did not input a Clan to Join.");
            return;
        }

        clan = this.getModule().getManager().searchClan(player, args[0], true);
        if (clan == null) {
            return;
        }

        if (!(this.canJoinClan(player, client, clan))) {
            return;
        }

        UtilServer.callEvent(new ClanJoinEvent(clan, player, client));
    }

    private boolean canJoinClan(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (clan.isAdmin()) {
                UtilMessage.message(player, "Clans", "You cannot join Admin Clans!");
                return false;
            }

            if (clan.isSquadFull(this.getModule().getManager())) {
                UtilMessage.simpleMessage(player, "Clans", "<var> has too many members/allies!", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.NEUTRAL)));
                return false;
            }

            if (!(clan.isRequested(RequestType.INVITATION, player.getUniqueId().toString()))) {
                UtilMessage.simpleMessage(player, "Clans", "You have not been invited to <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.NEUTRAL)));

                if (this.getInstanceByClass(Core.class).getManagerByClass(RechargeManager.class).add(player, UtilString.format("JoinAttempt-%s", clan.getName()), 300_000L, false)) {
                    this.getModule().getManager().messageClan(clan, "Clans", "<var> tried to join the Clan, but is not invited.", Collections.singletonList(ClanRelation.NEUTRAL.getSuffix() + player.getName()), null);
                }

                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanJoinEvent> getClassOfEvent() {
        return ClanJoinEvent.class;
    }

    @Override
    public void onEvent(final ClanJoinEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Client client = event.getClient();

        clan.removeRequest(RequestType.INVITATION, player.getUniqueId().toString());
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.REQUESTS);

        clan.addMember(new Member(player, (client.isAdministrating() ? MemberRole.LEADER : MemberRole.RECRUIT)));
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.MEMBERS);

        UtilMessage.simpleMessage(player, "Clans", "You joined <var>.", Collections.singletonList(this.getModule().getManager().getClanShortName(clan, ClanRelation.SELF)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has joined the Clan.", Collections.singletonList(ClanRelation.SELF.getSuffix() + player.getName()), Collections.singletonList(player.getUniqueId()));

        UtilLogger.log(Clans.class, "Clans", "Joins", UtilString.format("%s has joined %s", player.getName(), this.getModule().getManager().getClanFullName(clan, null)));
    }
}