package me.trae.clans.preference.types;

import me.trae.clans.Clans;
import me.trae.clans.preference.PreferenceManager;
import me.trae.core.preference.Preference;
import me.trae.core.preference.models.BooleanPreference;

public class DisplayTerritoryTitleBar extends Preference<Clans, PreferenceManager, Boolean> implements BooleanPreference {

    public DisplayTerritoryTitleBar(final PreferenceManager manager) {
        super(manager);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Toggles the display of the <gold>Title Bar</gold> when entering/leaving Clan Territory."
        };
    }
}