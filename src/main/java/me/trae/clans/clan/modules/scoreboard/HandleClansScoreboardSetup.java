package me.trae.clans.clan.modules.scoreboard;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.client.enums.Rank;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.scoreboard.ScoreboardBuilder;
import me.trae.core.scoreboard.containers.ScoreboardContainer;
import me.trae.core.scoreboard.events.ScoreboardSetupEvent;
import me.trae.core.server.ServerManager;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HandleClansScoreboardSetup extends SpigotListener<Clans, ClanManager> implements ScoreboardContainer {

    public HandleClansScoreboardSetup(final ClanManager manager) {
        super(manager);
    }

    @Override
    public ScoreboardBuilder getBuilder(final ScoreboardSetupEvent event) {
        final Player player = event.getPlayer();

        final ClientManager clientManager = this.getInstance(Core.class).getManagerByClass(ClientManager.class);

        return new ScoreboardBuilder(event) {
            @Override
            public String getTeamKey(final Player targetPlayer) {
                final Clan targetClan = getManager().getClanByPlayer(targetPlayer);
                if (targetClan != null) {
                    return String.format("!%s", targetClan.getName());
                }

                final Client targetClient = clientManager.getClientByPlayer(targetPlayer);

                final int ordinal = Rank.values().length - targetClient.getRank().ordinal();

                return String.format("@%s", ordinal);
            }

            @Override
            public String getTitle() {
                return "   " + getInstance(Core.class).getManagerByClass(ServerManager.class).getServerDisplayNameAndState() + "   ";
            }

            @Override
            public void registerLines() {
                final Clan playerClan = getManager().getClanByPlayer(player);

                addCustomLine(ChatColor.YELLOW, "Clan", playerClan != null ? ClanRelation.SELF.getSuffix() + playerClan.getDisplayName() : "No Clan");

                if (getManager().energyEnabled && playerClan != null) {
                    addBlankLine();

                    addCustomLine(ChatColor.YELLOW, "Clan Energy", playerClan.getEnergyRemainingString());
                }

                addBlankLine();

                addCustomLine(ChatColor.YELLOW, "Territory", getManager().getTerritoryClanNameForScoreboard(playerClan, getManager().getClanByLocation(player.getLocation())));

                UtilJava.call(getInstance().getManagerByClass(GamerManager.class).getGamerByPlayer(player), gamer -> {
                    if (gamer != null) {
                        addBlankLine();
                        addCustomLine(ChatColor.YELLOW, "Coins", String.format("<gold>%s", gamer.getCoinsString()));
                    }
                });

                UtilJava.call(getInstance().getManagerByClass(WorldEventManager.class).getActiveWorldEvent(), activeWorldEvent -> {
                    if (activeWorldEvent != null) {
                        addBlankLine();
                        addCustomLine(ChatColor.WHITE, "World Event", activeWorldEvent.getDisplayName());
                    }
                });
            }

            @Override
            public String getTeamPrefix(final Player player, final Player targetPlayer) {
                final Clan playerClan = getManager().getClanByPlayer(player);

                if (playerClan == null) {
                    return super.getTeamPrefix(player, targetPlayer);
                }

                final ClanRelation clanRelation = getManager().getClanRelationByClan(getManager().getClanByPlayer(targetPlayer), playerClan);

                final String clanName = UtilString.trim(playerClan.getDisplayName(), 11);

                return String.format("%s %s", clanRelation.getPrefix() + clanName, clanRelation.getSuffix());
            }

            @Override
            public String getTeamSuffix(final Player player, final Player targetPlayer) {
                final Clan playerClan = getManager().getClanByPlayer(player);
                final Clan targetPlayerClan = getManager().getClanByPlayer(targetPlayer);

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
                    dominancePointsString = String.format("<red>-%s", enemyByPlayerClan.getDominancePoints());
                }

                if (enemyByTargetPlayerClan.getDominancePoints() > 0) {
                    dominancePointsString = String.format("<green>+%s", enemyByTargetPlayerClan.getDominancePoints());
                }

                return String.format(" <gray>(%s<gray>)", dominancePointsString);
            }
        };
    }
}