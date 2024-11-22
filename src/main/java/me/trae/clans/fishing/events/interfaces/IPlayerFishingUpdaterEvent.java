package me.trae.clans.fishing.events.interfaces;

import me.trae.core.event.types.IPlayerEvent;
import org.bukkit.entity.FishHook;

public interface IPlayerFishingUpdaterEvent extends IPlayerEvent {

    FishHook getHook();
}