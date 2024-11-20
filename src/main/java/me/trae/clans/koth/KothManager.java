package me.trae.clans.koth;

import me.trae.clans.Clans;
import me.trae.clans.koth.commands.KothCommand;
import me.trae.core.framework.SpigotManager;

public class KothManager extends SpigotManager<Clans> {

    public KothManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new KothCommand(this));

        addModule(new Koth(this));
    }
}