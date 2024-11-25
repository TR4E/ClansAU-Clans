package me.trae.clans.world;

import me.trae.clans.Clans;
import me.trae.clans.world.modules.FasterNighttimeCycle;
import me.trae.clans.world.modules.HandleIronDoorInteract;
import me.trae.clans.world.modules.SpringBlock;
import me.trae.clans.world.modules.WaterBlock;
import me.trae.clans.world.modules.block.DisableBedrock;
import me.trae.clans.world.modules.block.DisableObsidian;
import me.trae.clans.world.modules.interaction.block.*;
import me.trae.clans.world.modules.interaction.item.DisableDrinkingPotions;
import me.trae.clans.world.modules.interaction.item.HandleFlintAndSteelInteract;
import me.trae.clans.world.modules.restriction.DisableCreeperExplosionBlockDamage;
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

        // Block Modules
        addModule(new DisableBedrock(this));
        addModule(new DisableObsidian(this));

        // Block Interaction
        addModule(new DisableAnvilInteraction(this));
        addModule(new DisableBeaconInteraction(this));
        addModule(new DisableBrewingStandInteraction(this));
        addModule(new DisableDispenserInteraction(this));
        addModule(new DisableEnchantmentTableInteraction(this));
        addModule(new DisableEnderChestInteraction(this));
        addModule(new DisableEnderPortalFrameInteraction(this));

        // Item Interaction
        addModule(new DisableDrinkingPotions(this));
        addModule(new HandleFlintAndSteelInteract(this));

        // Restriction Modules
        addModule(new DisableCreeperExplosionBlockDamage(this));

        // Clans Modules
        addModule(new FasterNighttimeCycle(this));
        addModule(new HandleIronDoorInteract(this));
        addModule(new SpringBlock(this));
        addModule(new WaterBlock(this));
    }
}