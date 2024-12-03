package me.trae.clans.clan.modules;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.events.ClanUpdaterEvent;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilServer;

public class HandleClanUpdater extends SpigotUpdater<Clans, ClanManager> {

    public HandleClanUpdater(final ClanManager manager) {
        super(manager);
    }

    @Update(delay = 100L)
    public void onUpdater() {
        for (final Clan clan : this.getManager().getClans().values()) {
            UtilServer.callEvent(new ClanUpdaterEvent(clan));
        }
    }
}