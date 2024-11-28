package me.trae.clans.crate.modules;

import me.trae.clans.Clans;
import me.trae.clans.crate.Crate;
import me.trae.clans.crate.CrateManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.events.ItemUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class HandleCrateItemStackUpdate extends SpigotListener<Clans, CrateManager> {

    public HandleCrateItemStackUpdate(final CrateManager manager) {
        super(manager);
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        final ItemStack itemStack = event.getBuilder().getItemStack();

        final Crate crate = this.getManager().getCrateByItemStack(itemStack);
        if (crate == null) {
            return;
        }

        event.setBuilder(crate.getItemBuilder());
    }
}