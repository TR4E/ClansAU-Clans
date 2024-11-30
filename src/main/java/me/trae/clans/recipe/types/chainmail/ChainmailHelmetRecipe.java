package me.trae.clans.recipe.types.chainmail;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.core.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChainmailHelmetRecipe extends CustomRecipe<Clans, RecipeManager> {

    public ChainmailHelmetRecipe(final RecipeManager manager) {
        super(manager, new ItemStack(Material.CHAINMAIL_HELMET));
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('E', new ItemStack(Material.EMERALD));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "EEE",
                "E E",
                "   "
        });

        this.addShape(new String[]{
                "   ",
                "EEE",
                "E E"
        });
    }
}