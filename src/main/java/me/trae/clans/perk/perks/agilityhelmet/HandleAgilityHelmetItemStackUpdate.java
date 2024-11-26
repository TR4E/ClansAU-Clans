package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.clans.Clans;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotSubListener;
import me.trae.core.item.events.ItemUpdateEvent;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class HandleAgilityHelmetItemStackUpdate extends SpigotSubListener<Clans, AgilityHelmet> {

    public HandleAgilityHelmetItemStackUpdate(final AgilityHelmet module) {
        super(module);
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        if (event.getBuilder().getItemStack().getType() != this.getModule().getMaterial()) {
            return;
        }

        event.getBuilder().setDisplayName(this.getModule().getName());
        event.getBuilder().setLore(new ArrayList<>(Arrays.asList(this.getModule().getDescription())));
    }
}