package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.fishing.events.interfaces.IPlayerFishingBiteEvent;
import me.trae.core.event.CustomEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class PlayerFishingBiteEvent extends CustomEvent implements IPlayerFishingBiteEvent {

    private final Player player;
    private final FishHook hook;

    public PlayerFishingBiteEvent(final PlayerFishingUpdaterEvent playerFishingUpdaterEvent) {
        this.player = playerFishingUpdaterEvent.getPlayer();
        this.hook = playerFishingUpdaterEvent.getHook();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public State getState() {
        return State.BITE;
    }

    @Override
    public FishHook getHook() {
        return this.hook;
    }
}