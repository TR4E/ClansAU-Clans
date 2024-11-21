package me.trae.clans.fishing;

import me.trae.clans.Clans;
import me.trae.clans.fishing.modules.HandleCustomFishing;
import me.trae.clans.fishing.modules.HandleFishItemBuilderUpdate;
import me.trae.clans.fishing.modules.HandlePrePlayerFish;
import me.trae.clans.fishing.modules.caught.*;
import me.trae.clans.fishing.modules.start.HandleFishHookRainInfluenced;
import me.trae.clans.fishing.modules.start.HandleFishHookSkyInfluenced;
import me.trae.clans.fishing.modules.start.HandleFishHookWaitAndLureTime;
import me.trae.core.framework.SpigotManager;

public class FishingManager extends SpigotManager<Clans> {

    public FishingManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Start Modules
        addModule(new HandleFishHookRainInfluenced(this));
        addModule(new HandleFishHookSkyInfluenced(this));
        addModule(new HandleFishHookWaitAndLureTime(this));

        // Caught Modules
        addModule(new HandleFishingCaughtReceive(this));
        addModule(new HandleFishingCaughtWeight(this));

        addModule(new HandlePlayerCaughtFish(this));
        addModule(new HandlePlayerCaughtLegendary(this));
        addModule(new HandlePlayerCaughtMob(this));

        addModule(new HandlePlayerCaughtFish(this));

        // Modules
        addModule(new HandleCustomFishing(this));
        addModule(new HandleFishItemBuilderUpdate(this));
        addModule(new HandlePrePlayerFish(this));
    }
}