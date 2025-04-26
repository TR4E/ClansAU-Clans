package me.trae.clans.world;

import me.trae.clans.Clans;
import me.trae.clans.world.commands.*;
import me.trae.clans.world.modules.*;
import me.trae.clans.world.modules.block.DisableBedrock;
import me.trae.clans.world.modules.block.DisableContainerBlockInSky;
import me.trae.clans.world.modules.block.DisableObsidian;
import me.trae.clans.world.modules.interaction.block.*;
import me.trae.clans.world.modules.interaction.item.DisableBoneMealInteract;
import me.trae.clans.world.modules.interaction.item.DisableDrinkingPotions;
import me.trae.clans.world.modules.interaction.item.HandleFlintAndSteelInteract;
import me.trae.clans.world.modules.restriction.DisableCreeperExplosionBlockDamage;
import me.trae.clans.world.modules.restriction.DisableMerchantInventory;
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
        // Commands
        addModule(new ArmourCommand(this));
        addModule(new DyeCommand(this));
        addModule(new LocationsCommand(this));
        addModule(new TrackCommand(this));
        addModule(new WarpCommand(this));

        // Core Modules
        addModule(new DisableSaturation<>(this));
        addModule(new DisableWeather<>(this));
        addModule(new LimitCreatureSpawn<>(this));

        // Block Modules
        addModule(new DisableBedrock(this));
        addModule(new DisableContainerBlockInSky(this));
        addModule(new DisableObsidian(this));

        // Block Interaction
        addModule(new DisableAnvilInteraction(this));
        addModule(new DisableBeaconInteraction(this));
        addModule(new DisableBedInteraction(this));
        addModule(new DisableBrewingStandInteraction(this));
        addModule(new DisableDispenserInteraction(this));
        addModule(new DisableEnchantmentTableInteraction(this));
        addModule(new DisableEnderChestInteraction(this));
        addModule(new DisableEnderPortalFrameInteraction(this));

        // Item Interaction
        addModule(new DisableBoneMealInteract(this));
        addModule(new DisableDrinkingPotions(this));
        addModule(new HandleFlintAndSteelInteract(this));

        // Restriction Modules
        addModule(new DisableCreeperExplosionBlockDamage(this));
        addModule(new DisableMerchantInventory(this));

        // Clans Modules
        addModule(new DisableBucketInteraction(this));
        addModule(new DisableHopperMoveItem(this));
        addModule(new HandleWorldTime(this));
        addModule(new HandleDropFallingBlock(this));
        addModule(new HandleFireballExplosion(this));
        addModule(new HandleIronDoorInteract(this));
        addModule(new HandleWoodDoorBlockPlace(this));
        addModule(new HandleWoodTrapDoorBlockPlace(this));
        addModule(new RemoveArrowsOnHit(this));
        addModule(new SpringBlock(this));
        addModule(new WaterBlock(this));
    }
}