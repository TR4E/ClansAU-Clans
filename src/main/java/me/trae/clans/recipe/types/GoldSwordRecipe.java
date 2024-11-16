package me.trae.clans.recipe.types;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldSwordRecipe extends CustomRecipe<Clans, RecipeManager> {

    public GoldSwordRecipe(final RecipeManager manager) {
        super(manager, new ItemStack(Material.GOLD_SWORD));
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('G', new ItemStack(Material.GOLD_BLOCK));
        this.addIngredient('S', new ItemStack(Material.STICK));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "G  ",
                "G  ",
                "S  "
        });

        this.addShape(new String[]{
                " G ",
                " G ",
                " S "
        });

        this.addShape(new String[]{
                "  G",
                "  G",
                "  S"
        });
    }

    @Override
    public boolean removeOriginalRecipe() {
        return true;
    }
}