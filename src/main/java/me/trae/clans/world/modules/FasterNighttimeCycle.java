package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.world.enums.TimeType;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class FasterNighttimeCycle extends SpigotUpdater<Clans, WorldManager> {

    public FasterNighttimeCycle(final WorldManager manager) {
        super(manager);

        this.addPrimitive("Duration", 20L);
    }

    @Update(asynchronous = true)
    public void onUpdater() {
        final long duration = this.getPrimitiveCasted(Long.class, "Duration");

        for (final World world : Bukkit.getWorlds()) {
            if (world.getTime() < TimeType.NIGHT.getDuration()) {
                continue;
            }

            world.setTime(world.getTime() + duration);
        }
    }
}