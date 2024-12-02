package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.enums.RequestType;
import me.trae.clans.clan.events.command.ClanTrustEvent;
import me.trae.clans.utility.constants.ClansArgumentType;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrustCommand extends ClanSubCommand implements EventContainer<ClanTrustEvent> {

    public TrustCommand(final ClanCommand command) {
        super(command, "trust");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Trust a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Trust.");
            return;
        }

        final Clan targetClan = this.getModule().getManager().searchClan(player, args[0], true);
        if (targetClan == null) {
            return;
        }

        if (!(this.canTrustClan(player, client, clan, targetClan))) {
            return;
        }

        if (!(client.isAdministrating())) {
            if (!(targetClan.isRequested(RequestType.TRUST, clan.getName()))) {
                this.requestTrust(player, clan, targetClan);
                return;
            }
        }

        this.callEvent(new ClanTrustEvent(clan, player, client, targetClan));
    }

    @Override
    public List<String> getTabCompletion(final Player player, final Client client, final Gamer gamer, final Clan clan, final String[] args) {
        if (clan != null) {
            if (args.length == 1) {
                return ClansArgumentType.CLAN_ALLIANCES.apply(clan, args[0]);
            }
        }

        return Collections.emptyList();
    }

    private boolean canTrustClan(final Player player, final Client client, final Clan playerClan, final Clan targetClan) {
        if (targetClan == playerClan) {
            UtilMessage.message(player, "Clans", "You cannot request an alliance with yourself!");
            return false;
        }

        if (!(targetClan.isAllianceByClan(playerClan))) {
            UtilMessage.simpleMessage(player, "Clans", "You are not allies with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, this.getModule().getManager().getClanRelationByClan(playerClan, targetClan))));
            return false;
        }

        if (targetClan.isTrustedByClan(playerClan)) {
            UtilMessage.simpleMessage(player, "Clans", "You are already trusted with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.TRUSTED_ALLIANCE)));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (targetClan.isAdmin()) {
                UtilMessage.message(player, "Clans", "You cannot request to trust with Admin Clans!");
                return false;
            }

            if (playerClan.isRequested(RequestType.TRUST, targetClan.getName())) {
                UtilMessage.simpleMessage(player, "Clans", "You already requested to trust with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ALLIANCE)));
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanTrustEvent> getClassOfEvent() {
        return ClanTrustEvent.class;
    }

    @Override
    public void onEvent(final ClanTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan playerClan = event.getClan();
        final Clan targetClan = event.getTarget();

        if (event.getClient().isAdministrating()) {
            this.forceTrust(playerClan, targetClan);
        } else {
            this.acceptTrust(event.getPlayer(), playerClan, targetClan);
        }
    }

    private void requestTrust(final Player player, final Clan playerClan, final Clan targetClan) {
        playerClan.addRequest(RequestType.TRUST, targetClan.getName());
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.REQUESTS);

        UtilMessage.simpleMessage(player, "Clans", "You requested to trust with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ALLIANCE)));

        this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has requested to trust with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ALLIANCE)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(targetClan, "Clans", "<var> has requested to trust with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.ALLIANCE)), null);
    }

    private void acceptTrust(final Player player, final Clan playerClan, final Clan targetClan) {
        this.handleTrust(playerClan, targetClan);

        UtilMessage.simpleMessage(player, "Clans", "You accepted to trust with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.TRUSTED_ALLIANCE)));

        this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has accepted to trust with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(targetClan, ClanRelation.TRUSTED_ALLIANCE)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(targetClan, "Clans", "<var> has accepted to trust with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.TRUSTED_ALLIANCE)), null);
    }

    private void forceTrust(final Clan playerClan, final Clan targetClan) {
        this.handleTrust(playerClan, targetClan);

        this.getModule().getManager().messageClan(playerClan, "Clans", "You are now trusted with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.TRUSTED_ALLIANCE)), null);
        this.getModule().getManager().messageClan(targetClan, "Clans", "You are now trusted with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.TRUSTED_ALLIANCE)), null);
    }

    private void handleTrust(final Clan playerClan, final Clan targetClan) {
        playerClan.removeRequest(RequestType.TRUST, targetClan.getName());
        targetClan.removeRequest(RequestType.TRUST, playerClan.getName());
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.REQUESTS);
        this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.REQUESTS);

        playerClan.getAllianceByClan(targetClan).setTrusted(true);
        targetClan.getAllianceByClan(playerClan).setTrusted(true);
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.ALLIANCES);
        this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ALLIANCES);
    }
}