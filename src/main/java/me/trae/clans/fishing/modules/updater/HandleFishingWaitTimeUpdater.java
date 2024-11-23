package me.trae.clans.fishing.modules.updater;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerFishingBiteEvent;
import me.trae.clans.fishing.events.PlayerFishingStartEvent;
import me.trae.clans.fishing.events.PlayerFishingUpdaterEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMath;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilTime;
import me.trae.core.utility.UtilTitle;
import me.trae.core.utility.objects.Pair;
import org.bukkit.ChatColor;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleFishingWaitTimeUpdater extends SpigotListener<Clans, FishingManager> {

    public HandleFishingWaitTimeUpdater(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFishingUpdater(final PlayerFishingUpdaterEvent event) {
        final FishHook hook = event.getHook();

        if (!(this.getManager().getWaitTimeHookMap().containsKey(hook))) {
            return;
        }

        final Player player = event.getPlayer();

        final Pair<Long, Long> pair = this.getManager().getWaitTimeHookMap().get(hook);

        final long systemTime = pair.getLeft();
        final long duration = pair.getRight() + 300L;

        if (UtilTime.elapsed(systemTime, duration)) {
            if (!(this.getManager().getBittenHooks().contains(hook))) {
                this.getManager().getBittenHooks().add(hook);
                UtilServer.callEvent(new PlayerFishingBiteEvent(event));
            }

            UtilTitle.sendActionBar(player, "<green><bold>Bitten!");

            if (UtilTime.elapsed(systemTime, duration + 500L)) {
                this.getManager().getWaitTimeHookMap().remove(hook);
                UtilServer.callEvent(new PlayerFishingStartEvent(event));
                this.getManager().getBittenHooks().remove(hook);
            }
            return;
        }

        final long remaining = UtilTime.getRemaining(systemTime, duration);

        UtilTitle.sendActionBar(player, UtilMath.getProgressBar(duration, remaining, 14.0D, '-', ChatColor.GREEN, ChatColor.GRAY));
    }
}