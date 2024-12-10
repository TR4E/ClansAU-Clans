package me.trae.clans.recipe.types.weapons;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldAxeRecipe extends CustomRecipe<Clans, RecipeManager> {

    public GoldAxeRecipe(final RecipeManager manager) {
        super(manager, new ItemStack(Material.GOLD_AXE));
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('G', new ItemStack(Material.GOLD_BLOCK));
        this.addIngredient('S', new ItemStack(Material.STICK));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "GG ",
                "GS ",
                " S "
        });

        this.addShape(new String[]{
                " GG",
                " SG",
                " S "
        });
    }

    @Override
    public boolean removeOriginalRecipe() {
        return true;
    }
}