package me.trae.clans.worldevent.modules;

import me.trae.clans.Clans;
import me.trae.clans.worldevent.WorldEvent;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilTime;

import java.util.ArrayList;
import java.util.List;

public class HandleRandomWorldEventUpdater extends SpigotUpdater<Clans, WorldEventManager> {

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "7_200_000")
    private long duration;

    private long systemTime;

    private final List<WorldEvent> LAST_WORLD_EVENTS = new ArrayList<>();

    public HandleRandomWorldEventUpdater(final WorldEventManager manager) {
        super(manager);

        this.systemTime = System.currentTimeMillis();
    }

    @Update(delay = 500L)
    public void onUpdater() {
        if (!(UtilTime.elapsed(this.systemTime, this.duration))) {
            return;
        }

        if (UtilServer.getOnlinePlayers().isEmpty()) {
            return;
        }

        this.systemTime = System.currentTimeMillis();

        if (this.getManager().getActiveWorldEvent() != null) {
            return;
        }

        final List<WorldEvent> worldEventList = this.getManager().getModulesByClass(WorldEvent.class);

        if (worldEventList.size() == this.LAST_WORLD_EVENTS.size()) {
            this.LAST_WORLD_EVENTS.clear();
        }

        for (final WorldEvent worldEvent : worldEventList) {
            if (this.LAST_WORLD_EVENTS.contains(worldEvent)) {
                continue;
            }

            this.LAST_WORLD_EVENTS.add(worldEvent);

            worldEvent.start();
            break;
        }
    }
}