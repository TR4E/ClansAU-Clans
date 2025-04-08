package me.trae.clans.worldevent.modules;

import me.trae.clans.Clans;
import me.trae.clans.worldevent.WorldEvent;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;

public class HandleWorldEventUpdater extends SpigotUpdater<Clans, WorldEventManager> {

    public HandleWorldEventUpdater(final WorldEventManager manager) {
        super(manager);
    }

    @Update(delay = 500L)
    public void onUpdater() {
        for (final WorldEvent worldEvent : this.getManager().getModulesByClass(WorldEvent.class)) {
            if (!(worldEvent.isActive())) {
                continue;
            }

            if (!(worldEvent.hasExpired())) {
                continue;
            }

            worldEvent.end();
        }
    }
}