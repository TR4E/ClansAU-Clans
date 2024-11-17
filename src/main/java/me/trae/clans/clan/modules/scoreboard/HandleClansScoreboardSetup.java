package me.trae.clans.clan.modules.scoreboard;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.gamer.GamerManager;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.client.enums.Rank;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.scoreboard.ScoreboardBuilder;
import me.trae.core.scoreboard.containers.ScoreboardContainer;
import me.trae.core.scoreboard.events.ScoreboardSetupEvent;
import me.trae.core.server.ServerManager;
import me.trae.core.utility.UtilColor;
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

        final Clan playerClan = this.getManager().getClanByPlayer(player);

        return new ScoreboardBuilder(event) {
            @Override
            public String getTeamKey(final Player target) {
                final Clan targetClan = getManager().getClanByPlayer(target);
                if (targetClan != null) {
                    return String.format("!%s", targetClan.getName());
                }

                final Client targetClient = clientManager.getClientByPlayer(target);
                final int ordinal = Rank.values().length - targetClient.getRank().ordinal();

                return String.format("@%s", ordinal);
            }

            @Override
            public String getTitle() {
                return "   " + getInstance(Core.class).getManagerByClass(ServerManager.class).getServerDisplayName() + "   ";
            }

            @Override
            public void registerLines() {
                addCustomLine(ChatColor.YELLOW, "Clan", playerClan != null ? ClanRelation.SELF.getSuffix() + playerClan.getName() : "No Clan");

                addBlankLine();

                addCustomLine(ChatColor.YELLOW, "Territory", getManager().getTerritoryClanNameForScoreboard(playerClan, getManager().getClanByLocation(player.getLocation())));

                addBlankLine();

                UtilJava.call(getInstance().getManagerByClass(GamerManager.class).getGamerByPlayer(player), gamer -> {
                    if (gamer != null) {
                        addCustomLine(ChatColor.YELLOW, "Coins", String.format("<gold>$%s", gamer.getCoins()));
                    }
                });
            }

            @Override
            public String getTeamPrefix(final Player player, final Client client, final Player target) {
                if (playerClan == null) {
                    return super.getTeamPrefix(player, client, target);
                }

                final ClanRelation clanRelation = getManager().getClanRelationByClan(getManager().getClanByPlayer(target), playerClan);

                final String clanName = UtilString.trim(playerClan.getName(), 11);

                return String.format("%s %s", clanRelation.getPrefix() + clanName, clanRelation.getSuffix());
            }
        };
    }
}