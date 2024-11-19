package me.trae.clans.fishing.events.interfaces;

public interface IPlayerStartFishingEvent extends IFishingEvent {

    long getWaitTime();

    void setWaitTime(final long minWaitTime, final long maxWaitTime);

    long getLureTime();

    void setLureTime(final long minLureTime, final long maxLureTime);

    boolean isSkyInfluenced();

    void setSkyInfluenced(final boolean skyInfluenced);

    boolean isRainInfluenced();

    void setRainInfluenced(final boolean weatherInfluenced);
}