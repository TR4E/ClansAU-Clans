package me.trae.clans.fishing.modules;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.CustomFishingEvent;
import me.trae.clans.fishing.events.PlayerCaughtFishEvent;
import me.trae.clans.fishing.events.PlayerStartFishingEvent;
import me.trae.clans.fishing.events.PlayerStopFishingEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleCustomFishing extends SpigotListener<Clans, FishingManager> {

    public HandleCustomFishing(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomFishing(final CustomFishingEvent event) {
        if (event.isCancelled()) {
            return;
        }

        switch (event.getState()) {
            case REEL_OUT:
                UtilServer.callEvent(new PlayerStartFishingEvent(event));
                break;
            case REEL_IN:
            case FAILED:
                this.getManager().WAIT_TIME_MAP.remove(event.getPlayer());
                UtilServer.callEvent(new PlayerStopFishingEvent(event));
                break;
            case CAUGHT:
                UtilServer.callEvent(new PlayerCaughtFishEvent(event));
                UtilServer.callEvent(new PlayerStopFishingEvent(event));
                break;
        }
    }
}