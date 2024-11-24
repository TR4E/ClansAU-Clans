package me.trae.clans.donation;

import me.trae.clans.Clans;
import me.trae.clans.config.Config;
import me.trae.core.donation.abstracts.AbstractDonationRepository;

public class DonationRepository extends AbstractDonationRepository<Clans, DonationManager, Config> {

    public DonationRepository(final DonationManager manager) {
        super(manager);
    }

    @Override
    public Class<Config> getClassOfConfiguration() {
        return Config.class;
    }
}