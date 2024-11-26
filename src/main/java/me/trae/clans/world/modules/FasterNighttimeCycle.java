package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.world.enums.TimeType;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class FasterNighttimeCycle extends SpigotUpdater<Clans, WorldManager> {

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "20")
    private long duration;

    public FasterNighttimeCycle(final WorldManager manager) {
        super(manager);
    }

    @Update(delay = 100L, asynchronous = true)
    public void onUpdater() {
        for (final World world : Bukkit.getWorlds()) {
            if (world.getTime() < TimeType.NIGHT.getDuration()) {
                continue;
            }

            world.setTime(world.getTime() + this.duration);
        }
    }
}