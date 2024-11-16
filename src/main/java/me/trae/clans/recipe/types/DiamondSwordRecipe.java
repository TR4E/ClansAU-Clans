package me.trae.clans.recipe.types;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondSwordRecipe extends CustomRecipe<Clans, RecipeManager> {

    public DiamondSwordRecipe(final RecipeManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('D', new ItemStack(Material.DIAMOND_BLOCK));
        this.addIngredient('S', new ItemStack(Material.STICK));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "D  ",
                "D  ",
                "S  "
        });

        this.addShape(new String[]{
                " D ",
                " D ",
                " S "
        });

        this.addShape(new String[]{
                "  D",
                "  D",
                "  S"
        });
    }

    @Override
    public boolean removeOriginalRecipe() {
        return true;
    }
}