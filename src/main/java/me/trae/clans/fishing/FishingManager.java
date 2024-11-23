package me.trae.clans.fishing;

import me.trae.clans.Clans;
import me.trae.clans.fishing.interfaces.IFishingManager;
import me.trae.clans.fishing.modules.HandleCustomFishing;
import me.trae.clans.fishing.modules.HandleFishItemBuilderUpdate;
import me.trae.clans.fishing.modules.HandlePrePlayerFish;
import me.trae.clans.fishing.modules.bite.HandlePlayerBiteFishSound;
import me.trae.clans.fishing.modules.caught.*;
import me.trae.clans.fishing.modules.start.HandleFishHookRainInfluenced;
import me.trae.clans.fishing.modules.start.HandleFishHookSkyInfluenced;
import me.trae.clans.fishing.modules.start.HandleFishHookWaitAndLureTime;
import me.trae.clans.fishing.modules.updater.HandleFishingWaitTimeUpdater;
import me.trae.core.framework.SpigotManager;
import me.trae.core.utility.objects.Pair;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishingManager extends SpigotManager<Clans> implements IFishingManager {

    private final Map<Player, FishHook> PLAYER_HOOK_MAP = new HashMap<>();
    private final Map<FishHook, Pair<Long, Long>> WAIT_TIME_HOOK_MAP = new HashMap<>();
    private final List<FishHook> BITTEN_HOOKS = new ArrayList<>();

    public FishingManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Bite Modules
        addModule(new HandlePlayerBiteFishSound(this));

        // Caught Modules
        addModule(new HandleFishingCaughtReceive(this));
        addModule(new HandleFishingCaughtWeight(this));

        addModule(new HandlePlayerCaughtFish(this));
        addModule(new HandlePlayerCaughtLegendary(this));
        addModule(new HandlePlayerCaughtMob(this));

        addModule(new HandlePlayerCaughtFish(this));

        // Start Modules
        addModule(new HandleFishHookRainInfluenced(this));
        addModule(new HandleFishHookSkyInfluenced(this));
        addModule(new HandleFishHookWaitAndLureTime(this));

        // Updater Modules
        addModule(new HandleFishingWaitTimeUpdater(this));

        // Modules
        addModule(new HandleCustomFishing(this));
        addModule(new HandleFishItemBuilderUpdate(this));
        addModule(new HandlePrePlayerFish(this));
    }

    @Override
    public Map<Player, FishHook> getPlayerHookMap() {
        return this.PLAYER_HOOK_MAP;
    }

    @Override
    public Map<FishHook, Pair<Long, Long>> getWaitTimeHookMap() {
        return this.WAIT_TIME_HOOK_MAP;
    }

    @Override
    public List<FishHook> getBittenHooks() {
        return this.BITTEN_HOOKS;
    }
}