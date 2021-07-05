package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TopCommand extends IClanCommand {

    public TopCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        UtilMessage.message(player, ChatColor.RED.toString() + ChatColor.BOLD + "---- Top 10 Clans -----");
        for (final String msg : this.getTopClans(player)) {
            UtilMessage.message(player, msg);
        }
    }

    private List<String> getTopClans(final Player player) {
        final List<String> list = new ArrayList<>();
        final Clan pClan = getManager().getClan(player.getUniqueId());
        final List<Clan> clans = new ArrayList<>(getManager().getClans().values());
        clans.sort((o1, o2) -> o2.getPoints() - o1.getPoints());
        int count = 0;
        for (final Clan clan : clans) {
            if (count > 10) {
                break;
            }
            count++;
            final ClanRelation rel = getManager().getClanRelation(pClan, clan);
            list.add(ChatColor.GRAY.toString() + count + ". " + rel.getSuffix() + getManager().getName(clan, true) + ChatColor.DARK_GRAY + " - " + ChatColor.RED + ChatColor.BOLD + clan.getPoints());
        }
        return list;
    }
}