package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanDisbandEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class DisbandCommand extends ClanSubCommand implements EventContainer<ClanDisbandEvent> {

    public DisbandCommand(final ClanCommand module) {
        super(module, "disband");
    }

    @Override
    public String getDescription() {
        return "Disband the Clan";
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.LEADER;
    }

    @Override
    public void execute(final Player player, final Client client, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(this.hasRequiredMemberRole(player, client, clan, true))) {
            return;
        }

        if (!(this.canDisbandClan(player, client, clan))) {
            return;
        }

        this.callEvent(new ClanDisbandEvent(clan, player, client));
    }

    private boolean canDisbandClan(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (clan.isAdmin()) {
                UtilMessage.message(player, "Clans", "You must be Administrating to disband Admin Clans!");
                return false;
            }

//            final Clan territoryClan = this.getModule().getManager().getClanByLocation(player.getLocation());
//            if (territoryClan != null && clan.isEnemyByClan(territoryClan)) {
//                UtilMessage.message(player, "Clans", "You cannot disband the clan while in enemy territory!");
//                return false;
//            }
//
//            if (clan.isBeingPillaged(this.getModule().getManager())) {
//                UtilMessage.message(player, "Clans", "You cannot disband the clan while being conquered by another clan!");
//                return false;
//            }
        }

        return true;
    }

    @Override
    public Class<ClanDisbandEvent> getClassOfEvent() {
        return ClanDisbandEvent.class;
    }

    @Override
    public void onEvent(final ClanDisbandEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        for (final Player target : UtilServer.getOnlinePlayers()) {
            final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByClan(this.getModule().getManager().getClanByPlayer(target), clan);

            UtilMessage.simpleMessage(target, "Clans", "<var> has disbanded <var>.", Arrays.asList(clanRelation.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(clan, clanRelation)));
        }

        this.getModule().getManager().disbandClan(clan);
    }
}