package me.trae.clans.recipe.types;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronDoorRecipe extends CustomRecipe<Clans, RecipeManager> {

    public IronDoorRecipe(final RecipeManager manager) {
        super(manager, new ItemStack(Material.IRON_DOOR, 2));
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('W', new ItemStack(Material.WOOD));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "WW ",
                "WW ",
                "WW "
        });

        this.addShape(new String[]{
                " WW",
                " WW",
                " WW"
        });
    }
}