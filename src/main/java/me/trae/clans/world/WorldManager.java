package me.trae.clans.world;

import me.trae.clans.Clans;
import me.trae.clans.world.modules.DisableSaturation;
import me.trae.core.framework.SpigotManager;

public class WorldManager extends SpigotManager<Clans> {

    public WorldManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Listeners
        addModule(new DisableSaturation(this));
    }
}