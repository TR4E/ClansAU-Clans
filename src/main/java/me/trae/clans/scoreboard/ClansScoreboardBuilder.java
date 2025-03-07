package me.trae.clans.scoreboard;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.scoreboard.interfaces.IClansScoreboard;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.scoreboard.ScoreboardBuilder;
import me.trae.core.scoreboard.events.ScoreboardSetupEvent;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class ClansScoreboardBuilder extends ScoreboardBuilder implements IClansScoreboard {

    public ClansScoreboardBuilder(final ScoreboardSetupEvent event) {
        super(event);
    }

    @Override
    public String getTeamKey(final Player targetPlayer) {
        final Clan targetClan = this.getClanManager().getClanByPlayer(targetPlayer);
        if (targetClan != null) {
            return UtilString.format("!%s", targetClan.getName());
        }

        final Client targetClient = this.getClientManager().getClientByPlayer(targetPlayer);

        final int ordinal = Rank.values().length - targetClient.getRank().ordinal();

        return UtilString.format("@%s", ordinal);
    }

    @Override
    public String getTitle() {
        return "   " + this.getServerManager().getServerDisplayNameAndState() + "   ";
    }

    @Override
    public void registerLines(final Player player, final Client client) {
        final Clan playerClan = this.getClanManager().getClanByPlayer(player);

        addCustomLine(ChatColor.YELLOW, "Clan", playerClan != null ? ClanRelation.SELF.getSuffix() + playerClan.getDisplayName() : "No Clan");

        if (this.getClanManager().energyEnabled && playerClan != null) {
            addBlankLine();

            addCustomLine(ChatColor.YELLOW, "Clan Energy", playerClan.getEnergyRemainingString());
        }

        addBlankLine();

        addCustomLine(ChatColor.YELLOW, "Territory", this.getClanManager().getTerritoryClanNameForScoreboard(playerClan, this.getClanManager().getClanByLocation(player.getLocation())));

        UtilJava.call(this.getGamerManager().getGamerByPlayer(player), gamer -> {
            if (gamer != null) {
                addBlankLine();
                addCustomLine(ChatColor.YELLOW, "Coins", UtilString.format("<gold>%s", gamer.getCoinsString()));
            }
        });

        UtilJava.call(this.getWorldEventManager().getActiveWorldEvent(), activeWorldEvent -> {
            if (activeWorldEvent != null) {
                addBlankLine();
                addCustomLine(ChatColor.WHITE, "World Event", activeWorldEvent.getDisplayName());
            }
        });
    }

    @Override
    public String getTeamPrefix(final Player player, final Player targetPlayer) {
        final Clan playerClan = this.getClanManager().getClanByPlayer(player);

        if (playerClan == null) {
            return super.getTeamPrefix(player, targetPlayer);
        }

        final ClanRelation clanRelation = this.getClanManager().getClanRelationByClan(this.getClanManager().getClanByPlayer(targetPlayer), playerClan);

        final String clanName = UtilString.trim(playerClan.getDisplayName(), 11);

        return UtilString.format("%s %s", clanRelation.getPrefix() + clanName, clanRelation.getSuffix());
    }

    @Override
    public String getTeamSuffix(final Player player, final Player targetPlayer) {
        final Clan playerClan = this.getClanManager().getClanByPlayer(player);
        final Clan targetPlayerClan = this.getClanManager().getClanByPlayer(targetPlayer);

        if (playerClan == null || targetPlayerClan == null) {
            return super.getTeamSuffix(player, targetPlayer);
        }

        final Enemy enemyByPlayerClan = playerClan.getEnemyByClan(targetPlayerClan);
        final Enemy enemyByTargetPlayerClan = targetPlayerClan.getEnemyByClan(playerClan);

        if (enemyByPlayerClan == null || enemyByTargetPlayerClan == null) {
            return super.getTeamSuffix(player, targetPlayer);
        }

        String dominancePointsString = "<white>0";

        if (enemyByPlayerClan.getDominancePoints() > 0) {
            dominancePointsString = UtilString.format("<red>-%s", enemyByPlayerClan.getDominancePoints());
        }

        if (enemyByTargetPlayerClan.getDominancePoints() > 0) {
            dominancePointsString = UtilString.format("<green>+%s", enemyByTargetPlayerClan.getDominancePoints());
        }

        return UtilString.format(" <gray>(%s<gray>)", dominancePointsString);
    }
}