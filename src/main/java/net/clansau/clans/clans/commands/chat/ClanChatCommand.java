package net.clansau.clans.clans.commands.chat;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.client.Rank;
import net.clansau.core.framework.command.Command;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class ClanChatCommand extends Command<ClanManager, Player> {

    public ClanChatCommand(final ClanManager manager) {
        super(manager, Player.class, "clanchat", new String[]{"cc"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }
        if (args == null || args.length == 0) {

        }
    }

    @Override
    protected void help(final Player player) {
    }
}