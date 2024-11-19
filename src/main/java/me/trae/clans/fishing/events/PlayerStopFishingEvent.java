package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.fishing.events.interfaces.IPlayerStopFishingEvent;
import me.trae.core.event.CustomCancellableEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class PlayerStopFishingEvent extends CustomCancellableEvent implements IPlayerStopFishingEvent {

    private final Player player;
    private final State state;
    private final FishHook hook;

    public PlayerStopFishingEvent(final CustomFishingEvent customFishingEvent) {
        this.player = customFishingEvent.getPlayer();
        this.state = customFishingEvent.getState();
        this.hook = customFishingEvent.getHook();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public FishHook getHook() {
        return this.hook;
    }
}