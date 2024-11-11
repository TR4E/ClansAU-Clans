package me.trae.clans.world;

import me.trae.clans.Clans;
import me.trae.clans.world.modules.*;
import me.trae.core.world.abstracts.AbstractWorldManager;
import me.trae.core.world.modules.shared.DisableSaturation;
import me.trae.core.world.modules.shared.DisableWeather;

public class WorldManager extends AbstractWorldManager<Clans> {

    public WorldManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Core Modules
        addModule(new DisableSaturation<>(this));
        addModule(new DisableWeather<>(this));

        // Disable Block Interaction
        addModule(new DisableAnvilInteraction(this));
        addModule(new DisableBeaconInteraction(this));
        addModule(new DisableBrewingStandInteraction(this));
        addModule(new DisableDispenserInteraction(this));
        addModule(new DisableEnchantmentTableInteraction(this));
        addModule(new DisableEnderChestInteraction(this));
        addModule(new DisableEnderPortalFrameInteraction(this));

        // Disable Block
        addModule(new DisableBedrock(this));
        addModule(new DisableObsidian(this));

        // Clans Modules
        addModule(new FasterNighttimeCycle(this));
    }
}