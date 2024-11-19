package me.trae.clans.worldevent;

import me.trae.clans.Clans;
import me.trae.clans.worldevent.commands.WorldEventCommand;
import me.trae.clans.worldevent.interfaces.IWorldEventManager;
import me.trae.clans.worldevent.modules.HandleWorldEventUpdater;
import me.trae.clans.worldevent.types.FishingFrenzy;
import me.trae.clans.worldevent.types.MiningMadness;
import me.trae.core.framework.SpigotManager;
import me.trae.core.utility.UtilJava;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class WorldEventManager extends SpigotManager<Clans> implements IWorldEventManager {

    private WorldEvent activeWorldEvent;

    public WorldEventManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new WorldEventCommand(this));

        // Modules
        addModule(new HandleWorldEventUpdater(this));

        // World Events
        addModule(new FishingFrenzy(this));
        addModule(new MiningMadness(this));
    }

    @Override
    public WorldEvent getActiveWorldEvent() {
        return this.activeWorldEvent;
    }

    @Override
    public void setActiveWorldEvent(final WorldEvent activeWorldEvent) {
        this.activeWorldEvent = activeWorldEvent;
    }

    @Override
    public boolean isActiveWorldEvent(final Class<? extends WorldEvent> clazz) {
        return this.getActiveWorldEvent() != null && this.getActiveWorldEvent().getClass().equals(clazz);
    }

    @Override
    public WorldEvent searchWorldEvent(final CommandSender sender, final String name, final boolean inform) {
        final List<Predicate<WorldEvent>> predicates = Arrays.asList(
                (worldEvent -> worldEvent.getSlicedName().equalsIgnoreCase(name)),
                (worldEvent -> worldEvent.getSlicedName().toLowerCase().contains(name.toLowerCase()))
        );

        final Function<WorldEvent, String> function = (WorldEvent::getName);

        return UtilJava.search(this.getModulesByClass(WorldEvent.class), predicates, null, function, "World Event Search", sender, name, inform);
    }
}