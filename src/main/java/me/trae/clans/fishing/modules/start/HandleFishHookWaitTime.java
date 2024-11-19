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

public class HandleFishHookWaitTime extends SpigotListener<Clans, FishingManager> {

    @ConfigInject(type = Long.class, path = "Min-Wait-Time", defaultValue = "5000")
    private long minWaitTime;

    @ConfigInject(type = Long.class, path = "Max-Wait-Time", defaultValue = "20000")
    private long maxWaitTime;

    @ConfigInject(type = Integer.class, path = "Frenzy-Luck-Percentage", defaultValue = "20")
    private int frenzyLuckPercentage;

    public HandleFishHookWaitTime(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerStartFishing_LOW(final PlayerStartFishingEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(event.getPlayer()).isAdministrating()) {
            event.setWaitTime(1000L, 5000L);
            return;
        }

        long minWaitTime = this.minWaitTime;
        long maxWaitTime = this.maxWaitTime;

        if (event.isFishingFrenzy()) {
            final double frenzyChance = this.frenzyLuckPercentage / 100.0;

            minWaitTime = (long) (minWaitTime * (1 - frenzyChance));
            maxWaitTime = (long) (maxWaitTime * (1 - frenzyChance));
        }

        event.setWaitTime(minWaitTime, maxWaitTime);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerStartFishing_MONITOR(final PlayerStartFishingEvent event) {
        if (event.isCancelled()) {
            return;
        }

        UtilFishHook.setWaitTime(event.getHook(), (int) (event.getWaitTime() / 50));
    }
}