package net.clansau.clans.scoreboard;

import net.clansau.clans.Clans;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.scoreboard.IScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClanScoreboard extends IScoreboard {

    public ClanScoreboard(final Clans instance, final Player player, final Location location) {
        super(instance, ChatColor.GOLD.toString() + ChatColor.BOLD + "   ClansAU Alpha   ");
        this.setupLines(player, location);
    }

    private void setupLines(final Player player, final Location location) {
        final ClanManager clanManager = getInstance().getManager(ClanManager.class);
        final Clan clan = clanManager.getClan(player.getUniqueId());
        setLine(0, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Clan");
        setLine(1, this.getClanName(clan));
        setLine(2, " ");
        setLine(3, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Territory");
        setLine(4, this.getTerritoryName(player, location, clanManager) + " ");
    }

    private String getClanName(final Clan clan) {
        if (clan == null) {
            return ChatColor.WHITE + "No Clan";
        }
        return ChatColor.AQUA + clan.getName();
    }

    private String getTerritoryName(final Player player, final Location location, final ClanManager clanManager) {
        if (clanManager.getClan(location) == null) {
            return ChatColor.GRAY + "Wilderness";
        }
        return getInstance().getManager(ClanManager.class).getTerritoryName(player, location);
    }
}