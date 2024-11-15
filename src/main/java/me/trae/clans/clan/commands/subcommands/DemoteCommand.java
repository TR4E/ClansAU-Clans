package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.MemberDemoteEvent;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class DemoteCommand extends ClanSubCommand implements EventContainer<MemberDemoteEvent> {

    public DemoteCommand(final ClanCommand command) {
        super(command, "demote");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <member>";
    }

    @Override
    public String getDescription() {
        return "Demote a Member";
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.LEADER;
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
            UtilMessage.message(player, "Clans", "You did not input a Member to Demote.");
            return;
        }

        final Client target = this.getModule().getManager().searchMember(player, clan, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canDemoteMember(player, client, clan, target))) {
            return;
        }

        this.callEvent(new MemberDemoteEvent(clan, player, client, target));
    }

    private boolean canDemoteMember(final Player player, final Client client, final Clan clan, final Client target) {
        if (!(client.isAdministrating())) {
            if (client == target) {
                UtilMessage.message(player, "Clans", "You cannot demote yourself!");
                return false;
            }

            if (target.isAdministrating() && !(client.hasRank(Rank.OWNER))) {
                UtilMessage.simpleMessage(player, "Clans", "You do not outrank <var>!", Collections.singletonList(ClanRelation.SELF.getSuffix() + target.getName()));
                return false;
            }
        }

        if (clan.getMemberByUUID(target.getUUID()).getRole() == MemberRole.getLowest()) {
            UtilMessage.simpleMessage(player, "Clans", "<var> cannot be demoted any further!", Collections.singletonList(ClanRelation.SELF.getSuffix() + target.getName()));
            return false;
        }

        return true;
    }

    @Override
    public Class<MemberDemoteEvent> getClassOfEvent() {
        return MemberDemoteEvent.class;
    }

    @Override
    public void onEvent(final MemberDemoteEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Client target = event.getTarget();

        final Member member = clan.getMemberByUUID(target.getUUID());

        member.setRole(MemberRole.getByOrdinal(member.getRole().ordinal() - 1));
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.MEMBERS);

        UtilMessage.simpleMessage(player, "Clans", "You have demoted <var> to <green><var></green>.", Arrays.asList(ClanRelation.SELF.getSuffix() + target.getName(), member.getRole().getName()));
        UtilMessage.simpleMessage(target.getPlayer(), "Clans", "<var> has demoted you to <green><var></green>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), member.getRole().getName()));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has demoted <var> to <green><var></green>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), ClanRelation.SELF.getSuffix() + target.getName(), member.getRole().getName()), Arrays.asList(player.getUniqueId(), target.getUUID()));
    }
}