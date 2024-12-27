package me.trae.clans.tnt;

import me.trae.clans.Clans;
import me.trae.clans.tnt.modules.HandleBlacklistedBlocksOnTntExplode;
import me.trae.clans.tnt.modules.HandleBlockHitByTnt;
import me.trae.clans.tnt.modules.HandlePreTntExplode;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.SpigotManager;

public class TntManager extends SpigotManager<Clans> {

    @ConfigInject(type = Double.class, path = "Radius", defaultValue = "3.0")
    public double radius;

    public TntManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new HandleBlacklistedBlocksOnTntExplode(this));
        addModule(new HandleBlockHitByTnt(this));
        addModule(new HandlePreTntExplode(this));
    }
}