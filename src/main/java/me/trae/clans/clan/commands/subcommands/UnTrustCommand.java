package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanRevokeTrustEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class UnTrustCommand extends ClanSubCommand implements EventContainer<ClanRevokeTrustEvent> {

    public UnTrustCommand(final ClanCommand command) {
        super(command, "untrust");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Revoke Trust a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Revoke Trust.");
            return;
        }

        final Clan targetClan = this.getModule().getManager().searchClan(player, args[0], true);
        if (targetClan == null) {
            return;
        }

        if (!(this.canRevokeTrustClan(player, clan, targetClan))) {
            return;
        }

        this.callEvent(new ClanRevokeTrustEvent(clan, player, client, targetClan));
    }

    private boolean canRevokeTrustClan(final Player player, final Clan clan, final Clan targetClan) {
        if (!(clan.isTrustedByClan(targetClan))) {
            UtilMessage.simpleMessage(player, "Clans", "You are not trusted with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, this.getModule().getManager().getClanRelationByClan(clan, targetClan))));
            return false;
        }

        return true;
    }

    @Override
    public Class<ClanRevokeTrustEvent> getClassOfEvent() {
        return ClanRevokeTrustEvent.class;
    }

    @Override
    public void onEvent(final ClanRevokeTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan playerClan = event.getClan();
        final Player player = event.getPlayer();
        final Clan targetClan = event.getTarget();

        this.handleRevokeTrust(playerClan, targetClan);

        UtilMessage.simpleMessage(player, "Clans", "You revoked trust with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ALLIANCE)));

        this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has revoked trust with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ALLIANCE)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(targetClan, "Clans", "<var> has revoked trust with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.ALLIANCE)), null);
    }

    private void handleRevokeTrust(final Clan playerClan, final Clan targetClan) {
        playerClan.getAllianceByClan(targetClan).setTrusted(false);
        targetClan.getAllianceByClan(playerClan).setTrusted(false);
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.ALLIANCES);
        this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ALLIANCES);
    }
}