package me.trae.clans.world.commands;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.clans.world.menus.WarpMenu;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMenu;
import org.bukkit.entity.Player;

public class WarpCommand extends Command<Clans, WorldManager> implements PlayerCommandType {

    public WarpCommand(final WorldManager manager) {
        super(manager, "warp", new String[0], Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        UtilMenu.open(new WarpMenu(this.getManager(), player));
    }
}