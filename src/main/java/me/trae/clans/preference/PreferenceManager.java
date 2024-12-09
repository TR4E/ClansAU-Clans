package me.trae.clans.preference;

import me.trae.clans.Clans;
import me.trae.clans.preference.types.DisplayTerritoryTitleBar;
import me.trae.clans.preference.types.SuperToolActivation;
import me.trae.core.preference.abstracts.AbstractPreferenceManager;

public class PreferenceManager extends AbstractPreferenceManager<Clans, PreferenceRepository> {

    public PreferenceManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        super.registerModules();

        addModule(new DisplayTerritoryTitleBar(this));
        addModule(new SuperToolActivation(this));
    }

    @Override
    public Class<PreferenceRepository> getClassOfRepository() {
        return PreferenceRepository.class;
    }
}