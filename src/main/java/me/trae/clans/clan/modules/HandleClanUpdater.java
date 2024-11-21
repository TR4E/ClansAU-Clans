package me.trae.clans.clan.modules;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.events.ClanUpdaterEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilTime;

public class HandleClanUpdater extends SpigotUpdater<Clans, ClanManager> {

    @ConfigInject(type = Long.class, path = "Delay", defaultValue = "0")
    private long delay;

    private long systemTime = System.currentTimeMillis();

    public HandleClanUpdater(final ClanManager manager) {
        super(manager);
    }

    @Update
    public void onUpdater() {
        if (this.delay != 0 && !(UtilTime.elapsed(this.systemTime, this.delay))) {
            return;
        }

        for (final Clan clan : this.getManager().getClans().values()) {
            UtilServer.callEvent(new ClanUpdaterEvent(clan));
        }

        this.systemTime = System.currentTimeMillis();
    }
}