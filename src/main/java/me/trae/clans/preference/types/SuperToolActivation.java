package me.trae.clans.preference.types;

import me.trae.clans.Clans;
import me.trae.clans.perk.perks.abstracts.SuperTool;
import me.trae.clans.preference.PreferenceManager;
import me.trae.core.perk.registry.PerkRegistry;
import me.trae.core.preference.Preference;
import me.trae.core.preference.models.BooleanPreference;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SuperToolActivation extends Preference<Clans, PreferenceManager, Boolean> implements BooleanPreference, Listener {

    public SuperToolActivation(final PreferenceManager manager) {
        super(manager);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Toggles the ability to instant mine with <gold>Super Tools</gold>."
        };
    }

    @Override
    public boolean showToPlayer(final Player player) {
        for (final SuperTool perk : PerkRegistry.getPerksByClass(SuperTool.class)) {
            if (!(perk.isUserByPlayer(player))) {
                continue;
            }

            return true;
        }

        return false;
    }
}