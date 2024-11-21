package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.command.MemberLeaveEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Collections;

public class LeaveCommand extends ClanSubCommand implements EventContainer<MemberLeaveEvent> {

    public LeaveCommand(final ClanCommand command) {
        super(command, "leave");
    }

    @Override
    public String getDescription() {
        return "Leave the Clan";
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.RECRUIT;
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(this.canLeaveClan(player, client, clan))) {
            return;
        }

        if (clan.getMembers().size() <= 1 && !(client.isAdministrating()) && !(clan.isAdmin())) {
            this.getModule().getSubModuleByClass(DisbandCommand.class).execute(player, client, gamer, args);
            return;
        }

        this.callEvent(new MemberLeaveEvent(clan, player, client));
    }

    private boolean canLeaveClan(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (clan.getMembers().size() > 1 && clan.getMemberByPlayer(player).hasRole(MemberRole.LEADER)) {
                UtilMessage.message(player, "Clans", "You must pass Leadership before leaving the Clan!");
                return false;
            }

            final Clan territoryClan = this.getModule().getManager().getClanByLocation(player.getLocation());
            if (territoryClan != null && clan.isEnemyByClan(territoryClan)) {
                UtilMessage.message(player, "Clans", "You cannot leave the clan while in enemy territory!");
                return false;
            }

            if (clan.isBeingPillaged(this.getModule().getManager())) {
                UtilMessage.message(player, "Clans", "You cannot leave the clan while being conquered by another clan!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<MemberLeaveEvent> getClassOfEvent() {
        return MemberLeaveEvent.class;
    }

    @Override
    public void onEvent(final MemberLeaveEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        clan.removeMember(clan.getMemberByPlayer(player));
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.MEMBERS);

        if (!(clan.isOnline())) {
            clan.setLastOnline(System.currentTimeMillis());
            this.getModule().getManager().getRepository().updateData(clan, ClanProperty.LAST_ONLINE);
        }

        this.getModule().getManager().removeClanChat(player);

        UtilMessage.simpleMessage(player, "Clans", "You left <var>.", Collections.singletonList(this.getModule().getManager().getClanShortName(clan, ClanRelation.NEUTRAL)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has left the Clan.", Collections.singletonList(ClanRelation.SELF.getSuffix() + player.getName()), null);
    }
}
