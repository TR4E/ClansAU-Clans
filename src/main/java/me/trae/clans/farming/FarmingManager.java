package me.trae.clans.farming;

import me.trae.clans.Clans;
import me.trae.clans.farming.modules.HandleFarmingZones;
import me.trae.core.framework.SpigotManager;

public class FarmingManager extends SpigotManager<Clans> {

    public FarmingManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new HandleFarmingZones(this));
    }
}