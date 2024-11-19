package me.trae.clans.worldevent.types;

import me.trae.clans.worldevent.WorldEvent;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.config.annotations.ConfigInject;

public class FishingFrenzy extends WorldEvent {

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "1_800_000")
    private long duration;

    public FishingFrenzy(final WorldEventManager manager) {
        super(manager);
    }

    @Override
    public long getDuration() {
        return this.duration;
    }
}