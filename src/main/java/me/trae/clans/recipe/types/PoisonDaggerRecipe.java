package me.trae.clans.recipe.types;

import me.trae.clans.Clans;
import me.trae.clans.recipe.RecipeManager;
import me.trae.clans.weapon.weapons.items.PoisonDagger;
import me.trae.core.recipe.CustomRecipe;
import me.trae.core.weapon.registry.WeaponRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PoisonDaggerRecipe extends CustomRecipe<Clans, RecipeManager> {

    public PoisonDaggerRecipe(final RecipeManager manager) {
        super(manager, WeaponRegistry.getWeaponByClass(PoisonDagger.class).getFinalBuilder().toItemStack());
    }

    @Override
    public void registerIngredients() {
        this.addIngredient('B', new ItemStack(Material.SLIME_BALL));
        this.addIngredient('I', new ItemStack(Material.IRON_INGOT));
        this.addIngredient('S', new ItemStack(Material.STICK));
    }

    @Override
    public void registerShapes() {
        this.addShape(new String[]{
                "B  ",
                "I  ",
                "S  "
        });

        this.addShape(new String[]{
                " B ",
                " I ",
                " S "
        });

        this.addShape(new String[]{
                "  B",
                "  I",
                "  S"
        });
    }
}