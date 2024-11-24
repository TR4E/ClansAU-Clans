package me.trae.clans.donation;

import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.core.donation.abstracts.AbstractDonationManager;

public class DonationManager extends AbstractDonationManager<Clans, DonationRepository, PerkManager> {

    public DonationManager(final Clans instance) {
        super(instance);
    }

    @Override
    public Class<DonationRepository> getClassOfRepository() {
        return DonationRepository.class;
    }

    @Override
    public PerkManager getPerkManager() {
        return this.getInstance(Clans.class).getManagerByClass(PerkManager.class);
    }
}