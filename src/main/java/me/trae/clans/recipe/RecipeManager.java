package me.trae.clans.recipe;

import me.trae.clans.Clans;
import me.trae.clans.recipe.modules.DisableTntRecipe;
import me.trae.clans.recipe.types.*;
import me.trae.core.recipe.abstracts.AbstractRecipeManager;

public class RecipeManager extends AbstractRecipeManager<Clans> {

    public RecipeManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Modules
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