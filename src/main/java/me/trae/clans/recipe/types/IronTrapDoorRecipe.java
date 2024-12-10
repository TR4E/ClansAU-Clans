package me.trae.clans.recipe.types;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronTrapDoorRecipe extends CustomRecipe<Clans, RecipeManager> {

    public IronTrapDoorRecipe(final RecipeManager manager) {
        super(manager, new ItemStack(Material.IRON_TRAPDOOR, 2));
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('W', new ItemStack(Material.WOOD));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "WWW",
                "WWW",
                "   "
        });

        this.addShape(new String[]{
                "   ",
                "WWW",
                "WWW"
        });
    }
}