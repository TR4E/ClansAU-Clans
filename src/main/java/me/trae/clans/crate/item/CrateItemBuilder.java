package me.trae.clans.crate.item;

import me.trae.clans.crate.Crate;
import me.trae.core.item.ItemBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class CrateItemBuilder extends ItemBuilder {

    public CrateItemBuilder(final Crate crate) {
        super(crate.getItemStack(), crate.getDisplayName(), new ArrayList<>(Arrays.asList(crate.getDescription())));
    }
}