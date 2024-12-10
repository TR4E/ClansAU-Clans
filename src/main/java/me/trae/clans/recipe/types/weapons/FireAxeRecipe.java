package me.trae.clans.recipe.types.weapons;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.clans.weapon.weapons.items.FireAxe;
import me.trae.core.recipe.CustomRecipe;
import me.trae.core.weapon.registry.WeaponRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FireAxeRecipe extends CustomRecipe<Clans, RecipeManager> {

    public FireAxeRecipe(final RecipeManager manager) {
        super(manager, WeaponRegistry.getWeaponByClass(FireAxe.class).getFinalBuilder().toItemStack());
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('G', new ItemStack(Material.GOLD_BLOCK));
        this.addIngredient('B', new ItemStack(Material.BLAZE_ROD));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "GG ",
                "GB ",
                " B "
        });

        this.addShape(new String[]{
                " GG",
                " BG",
                " B "
        });
    }
}