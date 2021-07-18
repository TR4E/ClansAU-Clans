package net.clansau.clans.fishing.enums;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum CatchType {

    BIG_CATCH("Big Catch"),
    HUGE_CATCH("Huge Catch"),
    STANDARD("Fishing");

    private final String prefix;

    CatchType(final String prefix) {
        this.prefix = prefix;
    }

    public final String getPrefix() {
        return this.prefix;
    }

    public void announce(final ClanManager clanManager, final Player player, final String caughtName) {
        final Clan clan = clanManager.getClan(player.getUniqueId());
        if (this.equals(STANDARD)) {
            UtilMessage.message(player, this.getPrefix(), (clan != null ? ChatColor.AQUA : ChatColor.YELLOW) + player.getName() + ChatColor.GRAY + " has caught " + ChatColor.GREEN + caughtName + ChatColor.GRAY + ".");
            return;
        }
        for (final Player online : Bukkit.getOnlinePlayers()) {
            final ClanRelation relation = clanManager.getClanRelation(clan, clanManager.getClan(online.getUniqueId()));
            UtilMessage.message(online, this.getPrefix(), relation.getSuffix() + player.getName() + ChatColor.GRAY + " has caught " + ChatColor.GREEN + caughtName + ChatColor.GRAY + ".");
        }
    }
}