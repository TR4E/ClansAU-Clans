package me.trae.clans.recipe;

import me.trae.clans.Clans;
import me.trae.clans.recipe.modules.DisableTNTRecipe;
import me.trae.core.recipe.abstracts.AbstractRecipeManager;

public class RecipeManager extends AbstractRecipeManager<Clans> {

    public RecipeManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new DisableTNTRecipe(this));
    }
}