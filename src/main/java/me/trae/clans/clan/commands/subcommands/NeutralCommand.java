package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.enums.RequestType;
import me.trae.clans.clan.events.command.ClanNeutralEvent;
import me.trae.clans.utility.constants.ClansArgumentType;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NeutralCommand extends ClanSubCommand implements EventContainer<ClanNeutralEvent> {

    public NeutralCommand(final ClanCommand command) {
        super(command, "neutral");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Neutral a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Neutral.");
            return;
        }

        final Clan targetClan = this.getModule().getManager().searchClan(player, args[0], true);
        if (targetClan == null) {
            return;
        }

        if (!(this.canNeutralClan(player, client, clan, targetClan))) {
            return;
        }

        final boolean forced = clan.isAllianceByClan(targetClan) || client.isAdministrating();

        if (!(forced)) {
            if (!(targetClan.isRequested(RequestType.NEUTRALITY, clan.getName()))) {
                this.requestNeutrality(player, clan, targetClan);
                return;
            }
        }

        this.callEvent(new ClanNeutralEvent(clan, player, client, targetClan));
    }

    @Override
    public List<String> getTabCompletion(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan != null) {
            if (args.length == 1) {
                final List<String> list = new ArrayList<>();

                list.addAll(ClansArgumentType.CLAN_ALLIANCES.apply(clan, args[0]));
                list.addAll(ClansArgumentType.CLAN_ENEMIES.apply(clan, args[0]));

                return list;
            }
        }

        return Collections.emptyList();
    }

    private boolean canNeutralClan(final Player player, final Client client, final Clan playerClan, final Clan targetClan) {
        if (targetClan == playerClan) {
            UtilMessage.message(player, "Clans", "You cannot request neutrality with yourself!");
            return false;
        }

        if (targetClan.isNeutralByClan(playerClan)) {
            UtilMessage.simpleMessage(player, "Clans", "You are already neutral with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.NEUTRAL)));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (targetClan.isAdmin()) {
                UtilMessage.message(player, "Clans", "You cannot request neutrality with Admin Clans!");
                return false;
            }

            if (playerClan.isRequested(RequestType.NEUTRALITY, targetClan.getName())) {
                UtilMessage.simpleMessage(player, "Clans", "You already requested neutrality with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, this.getModule().getManager().getClanRelationByClan(playerClan, targetClan))));
                return false;
            }

            if (targetClan.isPillageByClan(playerClan)) {
                UtilMessage.message(player, "Clans", "You cannot neutral a clan you are being pillaged by!");
                return false;
            }

            if (playerClan.isPillageByClan(targetClan)) {
                UtilMessage.message(player, "Clans", "You cannot neutral a clan you are pillaging!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanNeutralEvent> getClassOfEvent() {
        return ClanNeutralEvent.class;
    }

    @Override
    public void onEvent(final ClanNeutralEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan playerClan = event.getClan();
        final Player player = event.getPlayer();
        final Client client = event.getClient();
        final Clan targetClan = event.getTarget();

        if (playerClan.isAllianceByClan(targetClan) || client.isAdministrating()) {
            this.forceNeutrality(playerClan, targetClan);
        } else {
            this.acceptNeutrality(player, playerClan, targetClan);
        }
    }

    private void requestNeutrality(final Player player, final Clan playerClan, final Clan targetClan) {
        playerClan.addRequest(RequestType.NEUTRALITY, targetClan.getName());
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.REQUESTS);

        UtilMessage.simpleMessage(player, "Clans", "You requested neutrality with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, this.getModule().getManager().getClanRelationByClan(playerClan, targetClan))));

        this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has requested neutrality with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(targetClan, this.getModule().getManager().getClanRelationByClan(playerClan, targetClan))), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(targetClan, "Clans", "<var> has requested neutrality with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, this.getModule().getManager().getClanRelationByClan(targetClan, playerClan))), null);
    }

    private void acceptNeutrality(final Player player, final Clan playerClan, final Clan targetClan) {
        this.handleNeutral(playerClan, targetClan);

        UtilMessage.simpleMessage(player, "Clans", "You accepted neutrality with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.NEUTRAL)));

        this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has accepted neutrality with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(targetClan, ClanRelation.NEUTRAL)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(targetClan, "Clans", "<var> has accepted neutrality with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.NEUTRAL)), null);
    }

    private void forceNeutrality(final Clan playerClan, final Clan targetClan) {
        this.handleNeutral(playerClan, targetClan);

        this.getModule().getManager().messageClan(playerClan, "Clans", "You are now neutral with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.NEUTRAL)), null);
        this.getModule().getManager().messageClan(targetClan, "Clans", "You are now neutral with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.NEUTRAL)), null);
    }

    private void handleNeutral(final Clan playerClan, final Clan targetClan) {
        playerClan.removeRequest(RequestType.NEUTRALITY, targetClan.getName());
        targetClan.removeRequest(RequestType.NEUTRALITY, playerClan.getName());
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.REQUESTS);
        this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.REQUESTS);

        if (playerClan.isAllianceByClan(targetClan)) {
            playerClan.removeAlliance(playerClan.getAllianceByClan(targetClan));
            targetClan.removeAlliance(targetClan.getAllianceByClan(playerClan));

            this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.ALLIANCES);
            this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ALLIANCES);
        }

        if (playerClan.isEnemyByClan(targetClan)) {
            playerClan.removeEnemy(playerClan.getEnemyByClan(targetClan));
            targetClan.removeEnemy(targetClan.getEnemyByClan(playerClan));

            this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.ENEMIES);
            this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ENEMIES);
        }

        if (playerClan.isPillageByClan(targetClan)) {
            playerClan.removePillage(playerClan.getPillageByClan(targetClan));

            this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.PILLAGES);
        }

        if (targetClan.isPillageByClan(playerClan)) {
            targetClan.removePillage(targetClan.getPillageByClan(playerClan));

            this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.PILLAGES);
        }
    }
}