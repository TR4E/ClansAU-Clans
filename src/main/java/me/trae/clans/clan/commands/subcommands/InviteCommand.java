package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.enums.RequestType;
import me.trae.clans.clan.events.command.ClanInviteEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.constants.CoreArgumentType;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InviteCommand extends ClanSubCommand implements EventContainer<ClanInviteEvent> {

    public InviteCommand(final ClanCommand module) {
        super(module, "invite");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <player>";
    }

    @Override
    public String getDescription() {
        return "Invite a Player";
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.ADMIN;
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(this.hasRequiredMemberRole(player, client, clan, true))) {
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Clans", "You did not input a Player to Invite.");
            return;
        }

        final Player targetPlayer = UtilPlayer.searchPlayer(player, args[0], true);
        if (targetPlayer == null) {
            return;
        }

        if (!(this.canInvitePlayer(player, client, clan, targetPlayer))) {
            return;
        }

        UtilServer.callEvent(new ClanInviteEvent(clan, player, client, targetPlayer));
    }

    @Override
    public List<String> getTabCompletion(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan != null) {
            if (args.length == 1) {
                return CoreArgumentType.PLAYERS.apply(args[0]);
            }
        }

        return Collections.emptyList();
    }

    private boolean canInvitePlayer(final Player player, final Client client, final Clan clan, final Player targetPlayer) {
        if (targetPlayer == player) {
            UtilMessage.message(player, "Clans", "You cannot invite yourself!");
            return false;
        }

        final Clan targetClan = this.getModule().getManager().getClanByPlayer(targetPlayer);

        if (targetClan != null) {
            if (targetClan == clan) {
                UtilMessage.simpleMessage(player, "Clans", "<var> is already apart of the Clan!", Collections.singletonList(ClanRelation.SELF.getSuffix() + targetPlayer.getName()));
                return false;
            }

            final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByClan(clan, targetClan);

            UtilMessage.simpleMessage(player, "Clans", "<var> is already apart of <var>!", Arrays.asList(clanRelation.getSuffix() + targetPlayer.getName(), this.getModule().getManager().getClanFullName(targetClan, clanRelation)));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (clan.isRequested(RequestType.INVITATION, targetPlayer.getUniqueId().toString())) {
                UtilMessage.simpleMessage(player, "Clans", "You have already invited <var> to join the Clan!", Collections.singletonList(ClanRelation.NEUTRAL.getSuffix() + targetPlayer.getName()));
                return false;
            }

            if (clan.isSquadFull(this.getModule().getManager())) {
                UtilMessage.message(player, "Clans", "Your Clan has too many member/allies!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanInviteEvent> getClassOfEvent() {
        return ClanInviteEvent.class;
    }

    @Override
    public void onEvent(final ClanInviteEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Player targetPlayer = event.getTarget();

        clan.addRequest(RequestType.INVITATION, targetPlayer.getUniqueId().toString());
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.REQUESTS);

        UtilMessage.simpleMessage(player, "Clans", "You have invited <var> to join the Clan.", Collections.singletonList(ClanRelation.NEUTRAL.getSuffix() + targetPlayer.getName()));
        UtilMessage.simpleMessage(targetPlayer, "Clans", "<var> has invited you to join <var>.", Arrays.asList(ClanRelation.NEUTRAL.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(clan, ClanRelation.NEUTRAL)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has invited <var> to join the Clan.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), ClanRelation.NEUTRAL.getSuffix() + targetPlayer.getName()), Collections.singletonList(player.getUniqueId()));
    }
}