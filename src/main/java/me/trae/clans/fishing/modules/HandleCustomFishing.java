package me.trae.clans.fishing.modules;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.*;
import me.trae.clans.fishing.events.enums.State;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.updater.annotations.Update;
import me.trae.core.updater.interfaces.Updater;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilServer;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleCustomFishing extends SpigotListener<Clans, FishingManager> implements Updater {

    public HandleCustomFishing(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomFishing(final CustomFishingEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        final FishHook hook = event.getHook();

        if (event.getState() == State.REEL_OUT) {
            this.getManager().getHookMap().put(player, hook);
        } else {
            this.getManager().getHookMap().remove(player);
        }

        switch (event.getState()) {
            case REEL_OUT:
                UtilServer.callEvent(new PlayerStartFishingEvent(event));
                break;
            case REEL_IN:
            case FAILED:
                UtilServer.callEvent(new PlayerStopFishingEvent(event));
                break;
            case CAUGHT:
                UtilServer.callEvent(new PlayerCaughtFishEvent(event));
                UtilServer.callEvent(new PlayerStopFishingEvent(event));
                break;
        }
    }

    @Update
    public void onUpdater() {
        this.getManager().getHookMap().entrySet().removeIf(entry -> {
            final FishHook hook = entry.getValue();
            final Player player = entry.getKey();

            if (hook != null && player != null) {
                if (UtilBlock.isInWater(hook.getLocation())) {
                    UtilServer.callEvent(new PlayerFishingUpdaterEvent(player, hook));
                }

                return false;
            }

            return true;
        });
    }
}