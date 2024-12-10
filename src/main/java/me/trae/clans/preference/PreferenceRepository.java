package me.trae.clans.preference;

import me.trae.clans.Clans;
import me.trae.core.preference.abstracts.AbstractPreferenceRepository;

public class PreferenceRepository extends AbstractPreferenceRepository<Clans, PreferenceManager> {

    public PreferenceRepository(final PreferenceManager manager) {
        super(manager);
    }
}