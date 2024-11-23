package me.trae.clans.item;

import me.trae.clans.Clans;
import me.trae.clans.item.modules.RemoveEnchantmentsOnItemStack;
import me.trae.core.framework.SpigotManager;
import me.trae.core.item.modules.HandleItemStackDefaultDisplayNameChatColor;

public class ItemManager extends SpigotManager<Clans> {

    public ItemManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new RemoveEnchantmentsOnItemStack(this));
    }
}