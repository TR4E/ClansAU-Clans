package me.trae.clans.fishing.modules.start;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerFishingStartEvent;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.updater.interfaces.Updater;
import me.trae.core.utility.UtilFishHook;
import me.trae.core.utility.objects.Pair;
import org.bukkit.entity.FishHook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleFishHookWaitAndLureTime extends SpigotListener<Clans, FishingManager> implements Updater {

    @ConfigInject(type = Long.class, path = "Min-Wait-Time", defaultValue = "10_000")
    private long minWaitTime;

    @ConfigInject(type = Long.class, path = "Max-Wait-Time", defaultValue = "30_000")
    private long maxWaitTime;

    @ConfigInject(type = Long.class, path = "Min-Lure-Time", defaultValue = "2_000")
    private long minLureTime;

    @ConfigInject(type = Long.class, path = "Max-Lure-Time", defaultValue = "4_000")
    private long maxLureTime;

    @ConfigInject(type = Integer.class, path = "Frenzy-Luck-Percentage", defaultValue = "20")
    private int frenzyLuckPercentage;

    public HandleFishHookWaitAndLureTime(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerStartFishing_LOW(final PlayerFishingStartEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(event.getPlayer()).isAdministrating()) {
            event.setWaitTime(1000L, 5000L);
            return;
        }

        final Pair<Long, Long> waitPair = this.getTime(event, this.minWaitTime, this.maxWaitTime);
        final long minWaitTime = waitPair.getLeft();
        final long maxWaitTime = waitPair.getRight();

        event.setWaitTime(minWaitTime, maxWaitTime);

        final Pair<Long, Long> lurePair = this.getTime(event, this.minLureTime, this.maxLureTime);
        final long minLureTime = lurePair.getLeft();
        final long maxLureTime = lurePair.getRight();

        event.setLureTime(minLureTime, maxLureTime);
    }

    private Pair<Long, Long> getTime(final PlayerFishingStartEvent event, long minTime, long maxTime) {
        if (event.isFishingFrenzy()) {
            final double frenzyChance = this.frenzyLuckPercentage / 100.0;

            minTime = (long) (minTime * (1 - frenzyChance));
            maxTime = (long) (maxTime * (1 - frenzyChance));
        }

        return new Pair<>(minTime, maxTime);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerStartFishing_MONITOR(final PlayerFishingStartEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final FishHook hook = event.getHook();

        final long waitTime = event.getWaitTime();
        final long lureTime = waitTime + event.getLureTime();

        UtilFishHook.setWaitTime(hook, (int) (waitTime / 50));
        UtilFishHook.setLureTime(hook, (int) (lureTime / 50));

        this.getManager().getWaitTimeHookMap().put(hook, new Pair<>(System.currentTimeMillis(), lureTime));
    }
}