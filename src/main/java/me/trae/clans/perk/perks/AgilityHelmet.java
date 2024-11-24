package me.trae.clans.perk.perks;

import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.core.perk.Perk;

public class AgilityHelmet extends Perk<Clans, PerkManager> {

    public AgilityHelmet(final PerkManager manager) {
        super(manager);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Allow quick mobility.",
                "Left-Click with an empty hand to Leap."
        };
    }
}