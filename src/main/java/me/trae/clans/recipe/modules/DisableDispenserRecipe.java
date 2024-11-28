package me.trae.clans.recipe.modules;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.recipe.containers.DisableRecipeContainer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DisableDispenserRecipe extends SpigotListener<Clans, RecipeManager> implements DisableRecipeContainer {

    public DisableDispenserRecipe(final RecipeManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.DISPENSER);
    }
}