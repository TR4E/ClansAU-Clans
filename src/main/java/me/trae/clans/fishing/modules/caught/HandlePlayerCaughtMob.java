package me.trae.clans.fishing.modules.caught;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerFishingCaughtEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMath;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Arrays;
import java.util.List;

public class HandlePlayerCaughtMob extends SpigotListener<Clans, FishingManager> {

    private final List<EntityType> ENTITY_TYPES = Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CREEPER);

    @ConfigInject(type = Integer.class, path = "Min-Chance", defaultValue = "1")
    private int minChance;

    @ConfigInject(type = Integer.class, path = "Max-Chance", defaultValue = "1000")
    private int maxChance;

    @ConfigInject(type = Integer.class, path = "Base-Chance", defaultValue = "900")
    private int baseChance;

    public HandlePlayerCaughtMob(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCaughtFish(final PlayerFishingCaughtEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.isCaught()) {
            return;
        }

        if (!(event.isChance(this.minChance, this.maxChance, this.baseChance))) {
            return;
        }

        EntityType entityType = this.ENTITY_TYPES.get(UtilMath.getRandomNumber(Integer.class, 0, this.ENTITY_TYPES.size()));

        if (event.isChance(1, 100, 90)) {
            entityType = EntityType.GHAST;
        }

        final Entity entity = event.getHook().getWorld().spawnEntity(event.getHook().getLocation(), entityType);

        event.setCaught(entity);

        event.setCaughtName(ChatColor.YELLOW, entity.getName());
    }
}