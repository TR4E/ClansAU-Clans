package me.trae.clans.recipe;

import me.trae.clans.Clans;
import me.trae.clans.recipe.modules.*;
import me.trae.clans.recipe.types.*;
import me.trae.core.recipe.abstracts.AbstractRecipeManager;

public class RecipeManager extends AbstractRecipeManager<Clans> {

    public RecipeManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Modules
        addModule(new DisableBoatRecipe(this));
        addModule(new DisableBrewingStandRecipe(this));
        addModule(new DisableCompassRecipe(this));
        addModule(new DisableDispenserRecipe(this));
        addModule(new DisableDropperRecipe(this));
        addModule(new DisableEnchantedGoldenAppleRecipe(this));
        addModule(new DisableEnchantmentTableRecipe(this));
        addModule(new DisableGoldenAppleRecipe(this));
        addModule(new DisableGoldenCarrotRecipe(this));
        addModule(new DisablePistonRecipe(this));
        addModule(new DisableSlimeBlockRecipe(this));
        addModule(new DisableStickyPistonRecipe(this));
        addModule(new DisableTntRecipe(this));

        // Recipes
        addModule(new DiamondAxeRecipe(this));
        addModule(new DiamondSwordRecipe(this));
        addModule(new GoldAxeRecipe(this));
        addModule(new GoldSwordRecipe(this));

        addModule(new FireAxeRecipe(this));
        addModule(new PoisonDaggerRecipe(this));
    }
}