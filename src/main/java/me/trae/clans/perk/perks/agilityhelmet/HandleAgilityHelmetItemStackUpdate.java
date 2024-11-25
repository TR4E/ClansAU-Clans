package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.events.ItemUpdateEvent;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class HandleAgilityHelmetItemStackUpdate extends SpigotListener<Clans, PerkManager> {

    private final AgilityHelmet PERK;

    public HandleAgilityHelmetItemStackUpdate(final PerkManager manager) {
        super(manager);

        this.PERK = manager.getModuleByClass(AgilityHelmet.class);
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        if (event.getBuilder().getItemStack().getType() != this.PERK.getMaterial()) {
            return;
        }

        event.getBuilder().setDisplayName(this.PERK.getName());
        event.getBuilder().setLore(new ArrayList<>(Arrays.asList(this.PERK.getDescription())));
    }
}