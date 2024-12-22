package me.trae.clans.crate;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.crate.interfaces.ICrate;
import me.trae.clans.crate.loot.Loot;
import me.trae.core.framework.SpigotModule;
import me.trae.core.item.ItemBuilder;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMath;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Crate extends SpigotModule<Clans, CrateManager> implements ICrate {

    private final ItemStack itemStack;

    public Crate(final CrateManager manager, final ItemStack itemStack) {
        super(manager);

        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public String getDisplayName() {
        return this.getDisplayChatColor() + this.getName();
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return new ItemBuilder(this.getItemStack(), this.getDisplayName(), new ArrayList<>(Arrays.asList(this.getDescription())));
    }

    @Override
    public Loot getRandomLoot() {
        final List<Loot> list = this.getSubModulesByClass(Loot.class);

        if (this.getInstanceByClass().getManagerByClass(ClanManager.class).eotw) {
            return list.get(UtilMath.getRandomNumber(Integer.class, 0, list.size()));
        }

        double weight = 0.0D;

        for (final Loot loot : list) {
            weight += loot.getChance();
        }

        final double randomNumber = UtilMath.getRandomNumber(Double.class, 0.0D, weight);

        double sum = 0.0D;

        for (final Loot loot : list) {
            sum += loot.getChance();

            if (randomNumber <= sum) {
                return loot;
            }
        }

        return null;
    }

    @Override
    public void give(final Player player) {
        UtilItem.insert(player, this.getItemBuilder().toItemStack());
    }
}