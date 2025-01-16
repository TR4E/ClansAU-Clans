package me.trae.clans.clan.modules.scoreboard;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.scoreboard.ClansScoreboardBuilder;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.scoreboard.ScoreboardBuilder;
import me.trae.core.scoreboard.containers.ScoreboardContainer;
import me.trae.core.scoreboard.events.ScoreboardSetupEvent;
import me.trae.core.server.ServerManager;
import me.trae.core.utility.injectors.annotations.Inject;

public class HandleClansScoreboardSetup extends SpigotListener<Clans, ClanManager> implements ScoreboardContainer {

    @Inject
    private ClientManager clientManager;

    @Inject
    private ServerManager serverManager;

    @Inject
    private ClanManager clanManager;

    @Inject
    private GamerManager gamerManager;

    @Inject
    private WorldEventManager worldEventManager;

    public HandleClansScoreboardSetup(final ClanManager manager) {
        super(manager);
    }

    @Override
    public ScoreboardBuilder getBuilder(final ScoreboardSetupEvent setupEvent) {
        return new ClansScoreboardBuilder(setupEvent) {
            @Override
            public ClientManager getClientManager() {
                return clientManager;
            }

            @Override
            public ServerManager getServerManager() {
                return serverManager;
            }

            @Override
            public ClanManager getClanManager() {
                return clanManager;
            }

            @Override
            public GamerManager getGamerManager() {
                return gamerManager;
            }

            @Override
            public WorldEventManager getWorldEventManager() {
                return worldEventManager;
            }
        };
    }
}