package me.trae.clans.clan.commands;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.pillage.PillageStartEvent;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public class DominanceCommand extends Command<Clans, ClanManager> implements PlayerCommandType {

    public DominanceCommand(final ClanManager manager) {
        super(manager, "dominance", new String[0], Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        if (args.length == 0) {
            UtilMessage.message(player, "Dominance", "You did not input a Clan.");
            return;
        }

        final Clan clan = this.getManager().searchClan(player, args[0], true);
        if (clan == null) {
            return;
        }

        if (args.length == 1) {
            UtilMessage.message(player, "Dominance", "You did not input a Target Clan.");
            return;
        }

        final Clan targetClan = this.getManager().searchClan(player, args[1], true);
        if (targetClan == null) {
            return;
        }

        if (!(clan.isEnemyByClan(targetClan))) {
            UtilMessage.simpleMessage(player, "Dominance", "<var> and <var> are not enemies!", Arrays.asList(this.getManager().getClanFullName(clan, ClanRelation.NEUTRAL), this.getManager().getClanFullName(targetClan, ClanRelation.NEUTRAL)));
            return;
        }

        if (args.length == 2) {
            UtilMessage.message(player, "Dominance", "You did not input an Amount.");
            return;
        }

        final Optional<Integer> amountOptional = UtilString.getArgument(Integer.class, args[2]);
        if (!(amountOptional.isPresent())) {
            UtilMessage.message(player, "Dominance", "You did not input a valid Amount.");
            return;
        }

        final int amount = amountOptional.get();

        if (amount >= this.getManager().requiredPillagePoints) {
            UtilServer.callEvent(new PillageStartEvent(clan, targetClan));
            return;
        }

        final Enemy enemyByClan = clan.getEnemyByClan(targetClan);
        final Enemy enemyByTargetClan = targetClan.getEnemyByClan(clan);

        if (enemyByTargetClan.getDominancePoints() > 0) {
            enemyByTargetClan.setDominancePoints(0);
            this.getManager().getRepository().updateData(targetClan, ClanProperty.ENEMIES);
        }

        enemyByClan.setDominancePoints(amount);
        this.getManager().getRepository().updateData(clan, ClanProperty.ENEMIES);
    }
}