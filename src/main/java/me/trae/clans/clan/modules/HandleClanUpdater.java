package me.trae.clans.clan.modules;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.events.ClanUpdaterEvent;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HandleClanUpdater extends SpigotUpdater<Clans, ClanManager> {

    private List<Clan> CACHE;

    public HandleClanUpdater(final ClanManager manager) {
        super(manager);
    }

    @Update(delay = 100L)
    public void onUpdater() {
        final Collection<Clan> clanList = this.getManager().getClans().values();

        if (this.CACHE == null || this.CACHE.size() != clanList.size()) {
            this.CACHE = new ArrayList<>(clanList);
        }

        for (final Clan clan : this.CACHE) {
            UtilServer.callEvent(new ClanUpdaterEvent(clan));
        }
    }
}