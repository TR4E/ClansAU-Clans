package me.trae.clans.fishing.modules.start;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerStartFishingEvent;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilFishHook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleFishHookLureTime extends SpigotListener<Clans, FishingManager> {

    @ConfigInject(type = Long.class, path = "Min-Lure-Time", defaultValue = "20000")
    private long minLureTime;

    @ConfigInject(type = Long.class, path = "Max-Lure-Time", defaultValue = "40000")
    private long maxLureTime;

    @ConfigInject(type = Integer.class, path = "Frenzy-Luck-Percentage", defaultValue = "20")
    private int frenzyLuckPercentage;

    public HandleFishHookLureTime(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerStartFishing_LOW(final PlayerStartFishingEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(event.getPlayer()).isAdministrating()) {
            event.setLureTime(1000L, 5000L);
            return;
        }

        long minLureTime = this.minLureTime;
        long maxLureTime = this.maxLureTime;

        if (event.isFishingFrenzy()) {
            final double frenzyChance = this.frenzyLuckPercentage / 100.0;

            minLureTime = (long) (minLureTime * (1 - frenzyChance));
            maxLureTime = (long) (maxLureTime * (1 - frenzyChance));
        }

        event.setLureTime(minLureTime, maxLureTime);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerStartFishing_MONITOR(final PlayerStartFishingEvent event) {
        if (event.isCancelled()) {
            return;
        }

        UtilFishHook.setLureTime(event.getHook(), (int) (event.getLureTime() / 50));
    }
}