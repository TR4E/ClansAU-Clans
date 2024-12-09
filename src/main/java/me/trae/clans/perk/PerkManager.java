package me.trae.clans.perk;

import me.trae.clans.Clans;
import me.trae.clans.donation.DonationManager;
import me.trae.clans.perk.commands.CraftCommand;
import me.trae.clans.perk.commands.RepairCommand;
import me.trae.clans.perk.perks.*;
import me.trae.core.perk.abstracts.AbstractPerkManager;

public class PerkManager extends AbstractPerkManager<Clans, DonationManager> {

    public PerkManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new CraftCommand(this));
        addModule(new RepairCommand(this));

        // Perks
        addModule(new AgilityHelmet(this));
        addModule(new BuilderPackage(this));

        addModule(new SuperAxe(this));
        addModule(new SuperPickaxe(this));
        addModule(new SuperSpade(this));
    }

    @Override
    public DonationManager getDonationManager() {
        return this.getInstance(Clans.class).getManagerByClass(DonationManager.class);
    }
}