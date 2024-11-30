package me.trae.clans.quest.models.types;

import me.trae.clans.fields.events.FieldsBlockBreakEvent;
import me.trae.clans.quest.interfaces.IQuest;
import me.trae.clans.quest.models.CoinsQuestReward;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public interface MiningQuest extends IQuest, CoinsQuestReward, Listener {

    Material getMaterial();

    @EventHandler(priority = EventPriority.MONITOR)
    default void onFieldsBlockBreak(final FieldsBlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getBlock().getType() != this.getMaterial()) {
            return;
        }

        this.addProgress(event.getPlayer());
    }

    @Override
    default int getCategoryID() {
        return 2;
    }

    @Override
    default String getCategoryName() {
        return "Mining";
    }

    @Override
    default ItemStack getCategoryItemStack() {
        return new ItemStack(Material.IRON_PICKAXE);
    }
}