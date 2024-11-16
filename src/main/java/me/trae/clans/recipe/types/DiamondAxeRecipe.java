package me.trae.clans.recipe.types;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondAxeRecipe extends CustomRecipe<Clans, RecipeManager> {

    public DiamondAxeRecipe(final RecipeManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_AXE));
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('D', new ItemStack(Material.DIAMOND_BLOCK));
        this.addIngredient('S', new ItemStack(Material.STICK));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "DD ",
                "DS ",
                " S "
        });

        this.addShape(new String[]{
                " DD",
                " SD",
                " S "
        });
    }

    @Override
    public boolean removeOriginalRecipe() {
        return true;
    }
}