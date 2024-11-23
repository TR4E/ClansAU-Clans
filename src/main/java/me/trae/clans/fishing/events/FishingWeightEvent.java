package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.fishing.events.interfaces.IFishingEvent;
import me.trae.clans.fishing.events.interfaces.IFishingWeightEvent;
import me.trae.core.event.CustomEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class FishingWeightEvent extends CustomEvent implements IFishingWeightEvent {

    private final Player player;
    private final State state;
    private final FishHook hook;

    private int weight;

    public FishingWeightEvent(final IFishingEvent fishingEvent, final int weight) {
        this.player = fishingEvent.getPlayer();
        this.state = fishingEvent.getState();
        this.hook = fishingEvent.getHook();
        this.weight = weight;
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

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(final int weight) {
        this.weight = weight;
    }

    @Override
    public boolean hasWeight() {
        return this.getWeight() > 0;
    }
}