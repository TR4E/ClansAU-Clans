package me.trae.clans.perk;

import me.trae.clans.Clans;
import me.trae.clans.donation.DonationManager;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.perk.abstracts.AbstractPerkManager;

public class PerkManager extends AbstractPerkManager<Clans, DonationManager> {

    public PerkManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Perks
        addModule(new AgilityHelmet(this));
    }

    @Override
    public DonationManager getDonationManager() {
        return this.getInstance(Clans.class).getManagerByClass(DonationManager.class);
    }
}