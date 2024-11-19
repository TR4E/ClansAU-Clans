package me.trae.clans.worldevent.types;

import me.trae.clans.fields.events.FieldsLootEvent;
import me.trae.clans.worldevent.WorldEvent;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MiningMadness extends WorldEvent implements Listener {

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "1_800_000")
    private long duration;

    public MiningMadness(final WorldEventManager manager) {
        super(manager);
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFieldsLoot(final FieldsLootEvent event) {
        if (!(this.isActive())) {
            return;
        }

        event.setMultiplier(2);
    }
}