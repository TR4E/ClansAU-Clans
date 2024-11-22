package me.trae.clans.world;

import me.trae.clans.Clans;
import me.trae.clans.world.modules.*;
import me.trae.clans.world.modules.interaction.*;
import me.trae.core.world.abstracts.AbstractWorldManager;
import me.trae.core.world.modules.shared.DisableSaturation;
import me.trae.core.world.modules.shared.DisableWeather;
import me.trae.core.world.modules.shared.LimitCreatureSpawn;

public class WorldManager extends AbstractWorldManager<Clans> {

    public WorldManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Core Modules
        addModule(new DisableSaturation<>(this));
        addModule(new DisableWeather<>(this));
        addModule(new LimitCreatureSpawn<>(this));

        // Block Interaction
        addModule(new DisableAnvilInteraction(this));
        addModule(new DisableBeaconInteraction(this));
        addModule(new DisableBrewingStandInteraction(this));
        addModule(new DisableDispenserInteraction(this));
        addModule(new DisableEnchantmentTableInteraction(this));
        addModule(new DisableEnderChestInteraction(this));
        addModule(new DisableEnderPortalFrameInteraction(this));

        // Item Interaction
        addModule(new HandleFlintAndSteelInteract(this));

        // Disable Block
        addModule(new DisableBedrock(this));
        addModule(new DisableObsidian(this));

        // Clans Modules
        addModule(new DisableDrinkingPotions(this));
        addModule(new FasterNighttimeCycle(this));
        addModule(new HandleIronDoorInteract(this));
        addModule(new SpringBlock(this));
        addModule(new WaterBlock(this));
    }
}