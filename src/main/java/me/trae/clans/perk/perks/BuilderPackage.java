package me.trae.clans.perk.perks;

import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.core.client.Client;
import me.trae.core.perk.Perk;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilJava;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BuilderPackage extends Perk<Clans, PerkManager> {

    public BuilderPackage(final PerkManager manager) {
        super(manager);
    }

    private List<ItemStack> getContents() {
        return UtilJava.createCollection(new ArrayList<>(), list -> {
            list.add(new ItemStack(Material.DIAMOND_PICKAXE, 5));
            list.add(new ItemStack(Material.DIAMOND_SPADE, 5));

            list.add(new ItemStack(Material.IRON_PICKAXE, 10));
            list.add(new ItemStack(Material.IRON_SPADE, 10));

            list.add(new ItemStack(Material.LOG, 256));
            list.add(new ItemStack(Material.SPONGE, 12));
            list.add(new ItemStack(Material.GLASS, 64));
            list.add(new ItemStack(Material.GLOWSTONE, 64));
            list.add(new ItemStack(Material.BRICK, 64));

            list.add(new ItemStack(Material.SMOOTH_BRICK, 512));
            list.add(new ItemStack(Material.STONE, 128));
            list.add(new ItemStack(Material.SANDSTONE, 64, (byte) 2));
            list.add(new ItemStack(Material.NETHER_BRICK, 128));
        });
    }

    @Override
    public void onReceive(final Player player, final Client client) {
        for (final ItemStack itemStack : this.getContents()) {
            UtilItem.insert(player, itemStack);
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Claim a bundle of blocks at the start of every",
                "new map"
        };
    }
}