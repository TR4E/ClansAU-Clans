package me.trae.clans.fishing.interfaces;

import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

import java.util.Map;

public interface IFishingManager {

    Map<Player, FishHook> getHookMap();
}