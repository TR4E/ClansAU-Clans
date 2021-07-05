package net.clansau.clans.world;

import net.clansau.clans.Clans;
import net.clansau.clans.world.listeners.*;
import net.clansau.core.world.framework.IWorldManager;
import org.bukkit.Location;

public class WorldManager extends IWorldManager {

    public WorldManager(final Clans instance) {
        super(instance);
    }

    @Override
    protected void registerModules() {
        addModule(new DisableArmorStandInteract(this));
        addModule(new InteractIronDoors(this));
        addModule(new PreventBlockBreakInTerritory(this));
        addModule(new PreventBlockInteractInTerritory(this));
        addModule(new PreventBlockPlaceInTerritory(this));
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }
}