package me.trae.clans.item.modules;

import me.trae.clans.Clans;
import me.trae.clans.item.ItemManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.events.ItemUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class RemoveEnchantmentsOnItemStack extends SpigotListener<Clans, ItemManager> {

    public RemoveEnchantmentsOnItemStack(final ItemManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onItemUpdate(final ItemUpdateEvent event) {
        event.getBuilder().setRemoveEnchantments(true);
    }
}