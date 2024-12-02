package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.command.MemberKickEvent;
import me.trae.clans.utility.constants.ClansArgumentType;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KickCommand extends ClanSubCommand implements EventContainer<MemberKickEvent> {

    public KickCommand(final ClanCommand command) {
        super(command, "kick");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <member>";
    }

    @Override
    public String getDescription() {
        return "Kick a Member";
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
            UtilMessage.message(player, "Clans", "You did not input a Member to Kick.");
            return;
        }

        final Client target = this.getModule().getManager().searchMember(player, clan, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canKickMember(player, client, clan, target))) {
            return;
        }

        this.callEvent(new MemberKickEvent(clan, player, client, target));
    }

    @Override
    public List<String> getTabCompletion(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan != null) {
            if (args.length == 1) {
                return ClansArgumentType.CLAN_MEMBERS.apply(clan, args[0]);
            }
        }

        return Collections.emptyList();
    }

    private boolean canKickMember(final Player player, final Client client, final Clan playerClan, final Client targetClient) {
        if (targetClient == client) {
            UtilMessage.message(player, "Clans", "You cannot kick yourself!");
            return false;
        }

        if (!(client.isAdministrating())) {
            if (targetClient.isAdministrating() || playerClan.getMemberByUUID(targetClient.getUUID()).hasRole(playerClan.getMemberByPlayer(player).getRole())) {
                UtilMessage.simpleMessage(player, "Clans", "You do not outrank <var>!", Collections.singletonList(ClanRelation.SELF.getSuffix() + targetClient.getName()));
                return false;
            }

            if (playerClan.isBeingPillaged(this.getModule().getManager())) {
                UtilMessage.message(player, "Clans", "You cannot kick a member while being conquered by another clan!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<MemberKickEvent> getClassOfEvent() {
        return MemberKickEvent.class;
    }

    @Override
    public void onEvent(final MemberKickEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Client target = event.getTarget();

        clan.removeMember(clan.getMemberByUUID(target.getUUID()));
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.MEMBERS);

        UtilMessage.simpleMessage(player, "Clans", "You kicked <var> from the Clan.", Collections.singletonList(ClanRelation.NEUTRAL.getSuffix() + target.getName()));
        UtilMessage.simpleMessage(target.getPlayer(), "Clans", "<var> kicked you from the Clan.", Collections.singletonList(ClanRelation.NEUTRAL.getSuffix() + player.getName()));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> kicked <var> from the Clan.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), ClanRelation.NEUTRAL.getSuffix() + target.getName()), Collections.singletonList(player.getUniqueId()));
    }
}