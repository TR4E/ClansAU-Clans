package me.trae.clans.donation;

import me.trae.clans.Clans;
import me.trae.core.donation.abstracts.AbstractDonationRepository;

public class DonationRepository extends AbstractDonationRepository<Clans, DonationManager> {

    public DonationRepository(final DonationManager manager) {
        super(manager);
    }
}