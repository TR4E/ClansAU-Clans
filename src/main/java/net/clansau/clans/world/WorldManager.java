package net.clansau.clans.world;

import net.clansau.clans.Clans;
import net.clansau.core.world.framework.IWorldManager;
import org.bukkit.Location;

public class WorldManager extends IWorldManager {

    public WorldManager(final Clans instance) {
        super(instance);
    }

    @Override
    protected void registerModules() {
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }
}