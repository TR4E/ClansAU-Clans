package me.trae.clans.preference;

import me.trae.clans.Clans;
import me.trae.clans.config.Config;
import me.trae.core.preference.abstracts.AbstractPreferenceRepository;

public class PreferenceRepository extends AbstractPreferenceRepository<Clans, PreferenceManager, Config> {

    public PreferenceRepository(final PreferenceManager manager) {
        super(manager);
    }

    @Override
    public Class<Config> getClassOfConfiguration() {
        return Config.class;
    }
}