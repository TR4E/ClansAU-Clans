package me.trae.clans.crate.loot.models;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.Loot;
import me.trae.clans.crate.loot.models.interfaces.IPerkLoot;
import me.trae.core.Core;
import me.trae.core.donation.Donation;
import me.trae.core.donation.DonationManager;
import me.trae.core.perk.Perk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class PerkLoot extends Loot implements IPerkLoot {

    private final Perk<?, ?> perk;

    public PerkLoot(final Crate module, final double chance, final Perk<?, ?> perk) {
        super(module, chance, new ItemStack(Material.BOOK));

        this.perk = perk;
    }

    @Override
    public Perk<?, ?> getPerk() {
        return this.perk;
    }

    @Override
    public String getDisplayName() {
        return this.getPerk().getDisplayName() + " Perk";
    }

    @Override
    public Consumer<Player> getConsumer() {
        return (player -> {
            final Donation donation = new Donation(player.getUniqueId(), this.getPerk(), false);

            final DonationManager donationManager = this.getInstanceByClass(Core.class).getManagerByClass(DonationManager.class);

            donationManager.addDonation(donation);
            donationManager.getRepository().saveData(donation);
        });
    }
}