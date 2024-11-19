package me.trae.clans.fishing.events;

import me.trae.clans.fishing.events.enums.State;
import me.trae.clans.fishing.events.interfaces.IPlayerStartFishingEvent;
import me.trae.core.event.CustomCancellableEvent;
import me.trae.core.utility.UtilMath;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class PlayerStartFishingEvent extends CustomCancellableEvent implements IPlayerStartFishingEvent {

    private final Player player;
    private final State state;
    private final FishHook hook;

    private long waitTime, lureTime;
    private boolean skyInfluenced, weatherInfluenced;

    public PlayerStartFishingEvent(final CustomFishingEvent customFishingEvent) {
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

    @Override
    public long getWaitTime() {
        return this.waitTime;
    }

    @Override
    public void setWaitTime(final long minWaitTime, final long maxWaitTime) {
        this.waitTime = UtilMath.getRandomNumber(Long.class, minWaitTime, maxWaitTime);
    }

    @Override
    public long getLureTime() {
        return this.lureTime;
    }

    @Override
    public void setLureTime(final long minLureTime, final long maxLureTime) {
        this.lureTime = UtilMath.getRandomNumber(Long.class, minLureTime, maxLureTime);
    }

    @Override
    public boolean isSkyInfluenced() {
        return this.skyInfluenced;
    }

    @Override
    public void setSkyInfluenced(final boolean skyInfluenced) {
        this.skyInfluenced = skyInfluenced;
    }

    @Override
    public boolean isRainInfluenced() {
        return this.weatherInfluenced;
    }

    @Override
    public void setRainInfluenced(final boolean weatherInfluenced) {
        this.weatherInfluenced = weatherInfluenced;
    }
}