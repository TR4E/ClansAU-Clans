package net.clansau.clans.scoreboard;

import net.clansau.clans.Clans;
import net.clansau.core.scoreboard.IScoreboard;
import org.bukkit.ChatColor;

public class ClanScoreboard extends IScoreboard {

    public ClanScoreboard(final Clans instance) {
        super(instance, ChatColor.GOLD.toString() + ChatColor.BOLD + "ClansAU Season 1");
    }
}