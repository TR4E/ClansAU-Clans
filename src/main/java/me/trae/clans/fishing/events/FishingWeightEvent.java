package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.fishing.events.interfaces.IFishingWeightEvent;
import me.trae.core.event.CustomEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class FishingWeightEvent extends CustomEvent implements IFishingWeightEvent {

    private final Player player;
    private final State state;
    private final FishHook hook;

    private int weight;

    public FishingWeightEvent(final PlayerCaughtFishEvent playerCaughtFishEvent, final int weight) {
        this.player = playerCaughtFishEvent.getPlayer();
        this.state = playerCaughtFishEvent.getState();
        this.hook = playerCaughtFishEvent.getHook();
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