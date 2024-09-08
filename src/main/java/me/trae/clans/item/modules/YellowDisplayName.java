package me.trae.clans.item.modules;

import me.trae.clans.Clans;
import me.trae.clans.item.ItemManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.ItemBuilder;
import me.trae.core.item.events.ItemUpdateEvent;
import me.trae.core.utility.UtilColor;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class YellowDisplayName extends SpigotListener<Clans, ItemManager> {

    public YellowDisplayName(final ItemManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemUpdate(final ItemUpdateEvent event) {
        final ItemBuilder builder = event.getBuilder();

        if (!(builder.hasDisplayName())) {
            return;
        }

        if (UtilColor.isColorized(builder.getDisplayName())) {
            return;
        }

        builder.setDisplayName(UtilColor.applyIfNotMatched(ChatColor.YELLOW, builder.getDisplayName()));
    }
}