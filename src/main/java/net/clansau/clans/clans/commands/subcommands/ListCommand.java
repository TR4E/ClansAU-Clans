package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.stream.Collectors;

public class ListCommand extends IClanCommand {

    public ListCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        final Map<String, Clan> clans = getManager().getClans();
        if (clans.size() <= 0) {
            UtilMessage.message(player, "Clans", "There are currently no Clans that exist on this Server.");
            return;
        }
        UtilMessage.message(player, "Clans List", ChatColor.YELLOW.toString() + clans.size() + ChatColor.GRAY + " matches found [" + clans.values().stream().map(c -> getManager().getName(c, false)).collect(Collectors.joining(ChatColor.GRAY + ", ")) + ChatColor.GRAY + "].");
    }
}