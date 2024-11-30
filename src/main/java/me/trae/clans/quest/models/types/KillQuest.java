package me.trae.clans.quest.models.types;

import me.trae.api.death.events.CustomDeathEvent;
import me.trae.clans.quest.interfaces.IQuest;
import me.trae.clans.quest.models.CoinsQuestReward;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public interface KillQuest<T extends LivingEntity> extends IQuest, CoinsQuestReward, Listener {

    Class<T> getClassOfEntity();

    boolean canKill(final Player killer, final T entity);

    @EventHandler(priority = EventPriority.MONITOR)
    default void onCustomDeath(final CustomDeathEvent event) {
        if (!(event.getKiller() instanceof Player)) {
            return;
        }

        if (!(this.getClassOfEntity().isInstance(event.getEntity()))) {
            return;
        }

        final Player killer = event.getKillerByClass(Player.class);
        final T entity = event.getEntityByClass(this.getClassOfEntity());

        if (!(this.canKill(killer, entity))) {
            return;
        }

        this.addProgress(killer);
    }

    @Override
    default int getCategoryID() {
        return 1;
    }

    @Override
    default String getCategoryName() {
        return "Killing";
    }

    @Override
    default ItemStack getCategoryItemStack() {
        return new ItemStack(Material.IRON_SWORD);
    }
}