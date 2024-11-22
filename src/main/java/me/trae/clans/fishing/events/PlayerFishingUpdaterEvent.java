package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.interfaces.IPlayerFishingUpdaterEvent;
import me.trae.core.event.CustomCancellableEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class PlayerFishingUpdaterEvent extends CustomCancellableEvent implements IPlayerFishingUpdaterEvent {

    private final Player player;
    private final FishHook hook;

    public PlayerFishingUpdaterEvent(final Player player, final FishHook hook) {
        this.player = player;
        this.hook = hook;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public FishHook getHook() {
        return this.hook;
    }
}