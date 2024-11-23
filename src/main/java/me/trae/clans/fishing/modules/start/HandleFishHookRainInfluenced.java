package me.trae.clans.fishing.modules.start;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerFishingStartEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilFishHook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleFishHookRainInfluenced extends SpigotListener<Clans, FishingManager> {

    @ConfigInject(type = Boolean.class, path = "Rain-Influenced", defaultValue = "false")
    private boolean rainInfluenced;

    public HandleFishHookRainInfluenced(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerStartFishing_LOW(final PlayerFishingStartEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.setRainInfluenced(this.rainInfluenced);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerStartFishing_MONITOR(final PlayerFishingStartEvent event) {
        if (event.isCancelled()) {
            return;
        }

        UtilFishHook.setRainInfluenced(event.getHook(), event.isRainInfluenced());
    }
}