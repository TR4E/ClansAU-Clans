package me.trae.clans.fishing.modules;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.enums.FishName;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.ItemBuilder;
import me.trae.core.item.events.ItemUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class HandleFishItemBuilderUpdate extends SpigotListener<Clans, FishingManager> {

    public HandleFishItemBuilderUpdate(final FishingManager manager) {
        super(manager);
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        final ItemBuilder builder = event.getBuilder();

        final ItemStack itemStack = builder.getItemStack();

        if (!(FishName.isItemStack(itemStack))) {
            return;
        }

        builder.setDisplayName(itemStack.getItemMeta().getDisplayName());
    }
}