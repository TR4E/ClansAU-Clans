package me.trae.clans.fishing.interfaces;

import me.trae.core.utility.objects.Pair;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface IFishingManager {

    Map<Player, FishHook> getPlayerHookMap();

    Map<FishHook, Pair<Long, Long>> getWaitTimeHookMap();

    List<FishHook> getBittenHooks();
}