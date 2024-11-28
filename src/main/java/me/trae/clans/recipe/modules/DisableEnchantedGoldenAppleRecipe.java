package me.trae.clans.recipe.modules;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.recipe.containers.DisableRecipeContainer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DisableEnchantedGoldenAppleRecipe extends SpigotListener<Clans, RecipeManager> implements DisableRecipeContainer {

    public DisableEnchantedGoldenAppleRecipe(final RecipeManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);
    }
}