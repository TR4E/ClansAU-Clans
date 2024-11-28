package me.trae.clans.crate.loot.models;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.Loot;
import me.trae.clans.crate.loot.models.interfaces.IItemLoot;
import me.trae.core.utility.UtilItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemLoot extends Loot implements IItemLoot {

    public ItemLoot(final Crate module, final double chance, final ItemStack itemStack) {
        super(module, chance, itemStack);
    }

    @Override
    public String getDisplayName() {
        return UtilItem.getDisplayName(this.getItemStack(), false);
    }

    @Override
    public ItemStack toItemStack() {
        return this.getItemStack();
    }

    @Override
    public Consumer<Player> getConsumer() {
        return (player -> UtilItem.insert(player, this.toItemStack()));
    }
}