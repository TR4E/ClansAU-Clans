package net.clansau.clans.scoreboard;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.scoreboard.events.ScoreboardUpdateEvent;
import org.bukkit.event.EventHandler;

public class ScoreboardListener extends CoreListener<ClanManager> {

    public ScoreboardListener(final ClanManager manager) {
        super(manager, "Scoreboard Listener");
    }

    @EventHandler
    public void onScoreboardUpdate(final ScoreboardUpdateEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getLocation() == null) {
            return;
        }
        e.setScoreboard(new ClanScoreboard((Clans) getInstance(), e.getPlayer(), e.getLocation()));
    }
}