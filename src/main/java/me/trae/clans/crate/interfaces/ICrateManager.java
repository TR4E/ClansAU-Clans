package me.trae.clans.crate.interfaces;

import me.trae.clans.crate.Crate;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public interface ICrateManager {

    Crate getCrateByItemStack(final ItemStack itemStack);

    boolean isCrateByItemStack(final ItemStack itemStack);

    Crate searchCrate(final CommandSender sender, final String name, final boolean inform);
}