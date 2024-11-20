package me.trae.clans.preference;

import me.trae.clans.Clans;
import me.trae.clans.preference.types.TerritoryTitleBar;
import me.trae.core.preference.abstracts.AbstractPreferenceManager;

public class PreferenceManager extends AbstractPreferenceManager<Clans, PreferenceRepository> {

    public PreferenceManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        super.registerModules();

        addModule(new TerritoryTitleBar(this));
    }

    @Override
    public Class<PreferenceRepository> getClassOfRepository() {
        return PreferenceRepository.class;
    }
}