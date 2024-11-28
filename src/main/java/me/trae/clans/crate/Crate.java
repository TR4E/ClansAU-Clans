package me.trae.clans.crate;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.crate.interfaces.ICrate;
import me.trae.clans.crate.item.CrateItemBuilder;
import me.trae.clans.crate.loot.Loot;
import me.trae.core.framework.SpigotModule;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMath;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    public CrateItemBuilder getItemBuilder() {
        return new CrateItemBuilder(this);
    }

    @Override
    public Loot getLootByItemStack(final ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.hasItemMeta()) {
                for (final Loot loot : this.getSubModulesByClass(Loot.class)) {
                    if (!(UtilItem.isSimilarWithItemMeta(itemStack, loot.getItemBuilder().toItemStack()))) {
                        continue;
                    }

                    return loot;
                }
            }
        }

        return null;
    }

    @Override
    public boolean isLootByItemStack(final ItemStack itemStack) {
        return this.getLootByItemStack(itemStack) != null;
    }

    @Override
    public Loot getRandomLoot() {
        final List<Loot> list = this.getSubModulesByClass(Loot.class);

        if (this.getInstance().getManagerByClass(ClanManager.class).eotw) {
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