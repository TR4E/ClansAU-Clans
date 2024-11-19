package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.fishing.events.interfaces.IFishingEvent;
import me.trae.core.event.CustomCancellableEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class CustomFishingEvent extends CustomCancellableEvent implements IFishingEvent {

    private final Player player;
    private final State state;
    private final FishHook hook;

    public CustomFishingEvent(final Player player, final State state, final FishHook hook) {
        this.player = player;
        this.state = state;
        this.hook = hook;
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