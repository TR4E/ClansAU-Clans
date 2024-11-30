package me.trae.clans.quest.models.types;

import me.trae.clans.fishing.events.PlayerFishingCaughtEvent;
import me.trae.clans.quest.interfaces.IQuest;
import me.trae.clans.quest.models.CoinsQuestReward;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public interface FishingQuest extends IQuest, CoinsQuestReward, Listener {

    boolean canCatch(final Player player);

    @EventHandler(priority = EventPriority.MONITOR)
    default void onPlayerFishingCaught(final PlayerFishingCaughtEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        if (!(this.canCatch(player))) {
            return;
        }

        this.addProgress(player);
    }

    @Override
    default int getCategoryID() {
        return 3;
    }

    @Override
    default String getCategoryName() {
        return "Fishing";
    }

    @Override
    default ItemStack getCategoryItemStack() {
        return new ItemStack(Material.FISHING_ROD);
    }
}