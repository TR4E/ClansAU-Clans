package me.trae.clans.gamer;

import me.trae.clans.Clans;
import me.trae.clans.config.Config;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.gamer.abstracts.AbstractGamerRepository;

public class GamerRepository extends AbstractGamerRepository<Clans, GamerManager, Config, Gamer, GamerProperty> {

    public GamerRepository(final GamerManager manager) {
        super(manager);
    }

    @Override
    public Class<Config> getClassOfConfiguration() {
        return Config.class;
    }
}