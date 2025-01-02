package me.trae.clans.clan.modules.scoreboard;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.scoreboard.ClansScoreboardBuilder;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.scoreboard.ScoreboardBuilder;
import me.trae.core.scoreboard.containers.ScoreboardContainer;
import me.trae.core.scoreboard.events.ScoreboardSetupEvent;
import me.trae.core.server.ServerManager;

public class HandleClansScoreboardSetup extends SpigotListener<Clans, ClanManager> implements ScoreboardContainer {

    private ClientManager CLIENT_MANAGER;
    private ServerManager SERVER_MANAGER;

    private ClanManager CLAN_MANAGER;
    private GamerManager GAMER_MANAGER;
    private WorldEventManager WORLD_EVENT_MANAGER;

    public HandleClansScoreboardSetup(final ClanManager manager) {
        super(manager);
    }

    @Override
    public void onInitialize() {
        this.CLIENT_MANAGER = this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class);
        this.SERVER_MANAGER = this.getInstanceByClass(Core.class).getManagerByClass(ServerManager.class);

        this.CLAN_MANAGER = this.getInstance().getManagerByClass(ClanManager.class);
        this.GAMER_MANAGER = this.getInstance().getManagerByClass(GamerManager.class);
        this.WORLD_EVENT_MANAGER = this.getInstance().getManagerByClass(WorldEventManager.class);
    }

    @Override
    public void onShutdown() {
        this.CLIENT_MANAGER = null;
        this.SERVER_MANAGER = null;

        this.CLAN_MANAGER = null;
        this.GAMER_MANAGER = null;
        this.WORLD_EVENT_MANAGER = null;
    }

    @Override
    public ScoreboardBuilder getBuilder(final ScoreboardSetupEvent setupEvent) {
        return new ClansScoreboardBuilder(setupEvent) {
            @Override
            public ClientManager getClientManager() {
                return CLIENT_MANAGER;
            }

            @Override
            public ServerManager getServerManager() {
                return SERVER_MANAGER;
            }

            @Override
            public ClanManager getClanManager() {
                return CLAN_MANAGER;
            }

            @Override
            public GamerManager getGamerManager() {
                return GAMER_MANAGER;
            }

            @Override
            public WorldEventManager getWorldEventManager() {
                return WORLD_EVENT_MANAGER;
            }
        };
    }
}