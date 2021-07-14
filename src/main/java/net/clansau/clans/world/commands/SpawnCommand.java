package net.clansau.clans.world.commands;

import net.clansau.clans.world.WorldManager;
import net.clansau.core.client.Rank;
import net.clansau.core.world.commands.interfaces.ISpawnCommand;

public class SpawnCommand extends ISpawnCommand {

    public SpawnCommand(final WorldManager manager) {
        super(manager, false);
    }

    @Override
    public Rank getDefaultRequiredRank() {
        return Rank.PLAYER;
    }
}