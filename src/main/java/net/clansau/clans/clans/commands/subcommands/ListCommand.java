package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.client.Rank;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand extends IClanCommand {

    public ListCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(client.hasRank(Rank.ADMIN, true))) {
            return;
        }
        final List<Clan> clans = new ArrayList<>(getManager().getClans().values());
        if (clans.size() <= 0) {
            UtilMessage.message(player, "Clans", "There are currently no Clans that exist on this Server.");
            return;
        }
        clans.sort(this::compareTo);
        UtilMessage.message(player, "Clans List", ChatColor.YELLOW.toString() + clans.size() + ChatColor.GRAY + " matches found [" + clans.stream().map(c -> getManager().getClanRelation(getManager().getClan(player.getUniqueId()), c).getSuffix() + getManager().getName(c, false)).collect(Collectors.joining(ChatColor.GRAY + ", ")) + ChatColor.GRAY + "].");
    }

    private int compareTo(final Clan o1, final Clan o2) {
        if (o1 instanceof AdminClan && !(o2 instanceof AdminClan)) {
            return -1;
        }
        return 0;
    }
}