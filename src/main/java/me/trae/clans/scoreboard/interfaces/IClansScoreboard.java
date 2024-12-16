package me.trae.clans.scoreboard.interfaces;

import me.trae.clans.clan.ClanManager;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.client.ClientManager;
import me.trae.core.server.ServerManager;

public interface IClansScoreboard {

    ClientManager getClientManager();

    ServerManager getServerManager();

    ClanManager getClanManager();

    GamerManager getGamerManager();

    WorldEventManager getWorldEventManager();
}