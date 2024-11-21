package me.trae.clans.clan.modules.scoreboard;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.events.abstracts.types.ClanPlayerCancellableEvent;
import me.trae.clans.clan.events.abstracts.types.ClanPlayerEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleClansScoreboardUpdate extends SpigotListener<Clans, ClanManager> {

    public HandleClansScoreboardUpdate(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanPlayerEvent(final ClanPlayerEvent event) {
        UtilServer.callEvent(new ScoreboardUpdateEvent(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanPlayerCancellableEvent(final ClanPlayerCancellableEvent event) {
        if (event.isCancelled()) {
            return;
        }

        UtilServer.callEvent(new ScoreboardUpdateEvent(event.getPlayer()));
    }
}