package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.enums.RequestType;
import me.trae.clans.clan.events.ClanAllyEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class AllyCommand extends ClanSubCommand implements EventContainer<ClanAllyEvent> {

    public AllyCommand(final ClanCommand module) {
        super(module, "ally");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Ally a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Ally.");
            return;
        }

        final Clan targetClan = this.getModule().getManager().searchClan(player, args[0], true);
        if (targetClan == null) {
            return;
        }

        if (!(this.canAllyClan(player, client, clan, targetClan))) {
            return;
        }

        if (!(client.isAdministrating() && targetClan.isRequested(RequestType.ALLIANCE, clan.getName()))) {
            this.requestAlliance(player, clan, targetClan);
            return;
        }

        UtilServer.callEvent(new ClanAllyEvent(clan, player, client, targetClan));
    }

    private boolean canAllyClan(final Player player, final Client client, final Clan playerClan, final Clan targetClan) {
        if (targetClan == playerClan) {
            UtilMessage.message(player, "Clans", "You cannot request an alliance with yourself!");
            return false;
        }

        return true;
    }

    @Override
    public Class<ClanAllyEvent> getClassOfEvent() {
        return ClanAllyEvent.class;
    }

    @Override
    public void onEvent(final ClanAllyEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan playerClan = event.getClan();
        final Player player = event.getPlayer();
        final Client client = event.getClient();
        final Clan targetClan = event.getTarget();

        if (client.isAdministrating()) {
            this.forceAlliance(playerClan, targetClan);
        } else {
            this.acceptAlliance(player, playerClan, targetClan);
        }
    }

    private void handleAlliance(final Clan playerClan, final Clan targetClan) {
        playerClan.removeRequest(RequestType.ALLIANCE, targetClan.getName());
        targetClan.removeRequest(RequestType.ALLIANCE, playerClan.getName());
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.REQUESTS);
        this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.REQUESTS);

        playerClan.addAlliance(new Alliance(targetClan));
        targetClan.addAlliance(new Alliance(playerClan));
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.ALLIANCES);
        this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ALLIANCES);
    }

    private void requestAlliance(final Player player, final Clan playerClan, final Clan targetClan) {
        playerClan.addRequest(RequestType.ALLIANCE, targetClan.getName());
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.REQUESTS);

        UtilMessage.simpleMessage(player, "Clans", "You requested an alliance with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.NEUTRAL)));

        this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has requested an alliance with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(targetClan, ClanRelation.NEUTRAL)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(targetClan, "Clans", "<var> has requested an alliance with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.NEUTRAL)), null);
    }

    private void acceptAlliance(final Player player, final Clan playerClan, final Clan targetClan) {
        this.handleAlliance(playerClan, targetClan);

        UtilMessage.simpleMessage(player, "Clans", "You accepted alliance with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ALLIANCE)));

        this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has accepted an alliance with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ALLIANCE)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(targetClan, "Clans", "<var> has accepted an alliance with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.ALLIANCE)), null);
    }

    private void forceAlliance(final Clan playerClan, final Clan targetClan) {
        this.handleAlliance(playerClan, targetClan);

        this.getModule().getManager().messageClan(playerClan, "Clans", "You are now allies with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ALLIANCE)), null);
        this.getModule().getManager().messageClan(targetClan, "Clans", "You are now allies with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.ALLIANCE)), null);
    }
}