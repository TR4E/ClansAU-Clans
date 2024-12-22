package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.world.enums.TimeType;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class HandleWorldTime extends SpigotUpdater<Clans, WorldManager> {

    @ConfigInject(type = Long.class, path = "Faster-Night-Time-Duration", defaultValue = "20")
    private long fasterNightTimeDuration;

    @ConfigInject(type = Boolean.class, path = "Always-Day", defaultValue = "false")
    private boolean alwaysDay;

    @ConfigInject(type = Boolean.class, path = "Always-Night", defaultValue = "false")
    private boolean alwaysNight;

    public HandleWorldTime(final WorldManager manager) {
        super(manager);
    }

    @Update(delay = 100L, asynchronous = true)
    public void onUpdater() {
        for (final World world : Bukkit.getWorlds()) {
            if (this.alwaysDay) {
                if (world.getTime() != TimeType.NOON.getDuration()) {
                    world.setTime(TimeType.NOON.getDuration());
                }
                continue;
            }

            if (this.alwaysNight) {
                if (world.getTime() != TimeType.MIDNIGHT.getDuration()) {
                    world.setTime(TimeType.MIDNIGHT.getDuration());
                }
                continue;
            }

            if (this.fasterNightTimeDuration > 0L) {
                if (world.getTime() < TimeType.NIGHT.getDuration()) {
                    continue;
                }

                world.setTime(world.getTime() + this.fasterNightTimeDuration);
            }
        }
    }
}