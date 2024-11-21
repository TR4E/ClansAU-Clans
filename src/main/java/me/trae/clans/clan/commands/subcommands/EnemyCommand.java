package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.command.ClanEnemyEvent;
import me.trae.core.client.Client;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.containers.EventContainer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class EnemyCommand extends ClanSubCommand implements EventContainer<ClanEnemyEvent> {

    public EnemyCommand(final ClanCommand command) {
        super(command, "enemy");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Enemy a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Enemy.");
            return;
        }

        final Clan targetClan = this.getModule().getManager().searchClan(player, args[0], true);
        if (targetClan == null) {
            return;
        }

        if (!(this.canEnemyClan(player, client, clan, targetClan))) {
            return;
        }

        this.callEvent(new ClanEnemyEvent(clan, player, client, targetClan));
    }

    private boolean canEnemyClan(final Player player, final Client client, final Clan clan, final Clan targetClan) {
        if (clan == targetClan) {
            UtilMessage.message(player, "Clans", "You cannot enemy yourself!");
            return false;
        }

        if (clan.isEnemyByClan(targetClan)) {
            UtilMessage.simpleMessage(player, "Clans", "You are already enemies with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ENEMY)));
            return false;
        }

        if (!(clan.isNeutralByClan(targetClan))) {
            UtilMessage.simpleMessage(player, "Clans", "You must be neutral with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, this.getModule().getManager().getClanRelationByClan(clan, targetClan))));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (clan.isAdmin()) {
                UtilMessage.message(player, "Clans", "You cannot enemy Admin Clans!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanEnemyEvent> getClassOfEvent() {
        return ClanEnemyEvent.class;
    }

    @Override
    public void onEvent(final ClanEnemyEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan playerClan = event.getClan();
        final Player player = event.getPlayer();
        final Clan targetClan = event.getTarget();

        this.handleEnemy(playerClan, targetClan);

        UtilMessage.simpleMessage(player, "Clans", "You waged war with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ENEMY)));

        this.getModule().getManager().messageClan(playerClan, "Clans", "<var> has waged war with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(targetClan, ClanRelation.ENEMY)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(targetClan, "Clans", "<var> has waged war with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(playerClan, ClanRelation.ENEMY)), null);
    }

    private void handleEnemy(final Clan playerClan, final Clan targetClan) {
        playerClan.addEnemy(new Enemy(targetClan));
        targetClan.addEnemy(new Enemy(playerClan));
        this.getModule().getManager().getRepository().updateData(playerClan, ClanProperty.ENEMIES);
        this.getModule().getManager().getRepository().updateData(targetClan, ClanProperty.ENEMIES);
    }
}