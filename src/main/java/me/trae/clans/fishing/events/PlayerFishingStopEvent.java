package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.fishing.events.interfaces.IFishingEvent;
import me.trae.clans.fishing.events.interfaces.IPlayerFishingStopEvent;
import me.trae.core.event.CustomCancellableEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class PlayerFishingStopEvent extends CustomCancellableEvent implements IPlayerFishingStopEvent {

    private final Player player;
    private final State state;
    private final FishHook hook;

    public PlayerFishingStopEvent(final IFishingEvent fishingEvent) {
        this.player = fishingEvent.getPlayer();
        this.state = fishingEvent.getState();
        this.hook = fishingEvent.getHook();
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