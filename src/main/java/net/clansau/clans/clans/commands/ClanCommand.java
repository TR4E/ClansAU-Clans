package net.clansau.clans.clans.commands;

import net.clansau.clans.clans.ClanManager;
import net.clansau.core.client.Rank;
import net.clansau.core.framework.command.Command;
import org.bukkit.entity.Player;

public class ClanCommand extends Command<ClanManager, Player> {

    public ClanCommand(final ClanManager manager) {
        super(manager, Player.class, "clan", new String[]{"c", "f", "fac", "faction", "gang", "g"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            this.clanCommand(player, args);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "help": {
                help(player);
                break;
            }
            default: {

            }
        }
    }

    @Override
    protected void help(final Player player) {
    }

    private void clanCommand(final Player player, final String[] args) {

    }
}