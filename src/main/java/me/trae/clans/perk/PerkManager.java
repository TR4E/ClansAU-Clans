package me.trae.clans.perk;

import me.trae.clans.Clans;
import me.trae.clans.donation.DonationManager;
import me.trae.clans.perk.commands.CraftCommand;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.clans.perk.perks.agilityhelmet.*;
import me.trae.core.perk.abstracts.AbstractPerkManager;

public class PerkManager extends AbstractPerkManager<Clans, DonationManager> {

    public PerkManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new CraftCommand(this));

        // Perks
        addModule(new AgilityHelmet(this));

        // Agility Helmet Modules
        addModule(new HandleAgilityHelmetActivate(this));
        addModule(new HandleAgilityHelmetEquip(this));
        addModule(new HandleAgilityHelmetFallDamage(this));
        addModule(new HandleAgilityHelmetItemStackUpdate(this));
        addModule(new HandleAgilityHelmetOnRespawn(this));
        addModule(new HandleAgilityHelmetUpdater(this));
    }

    @Override
    public DonationManager getDonationManager() {
        return this.getInstance(Clans.class).getManagerByClass(DonationManager.class);
    }
}