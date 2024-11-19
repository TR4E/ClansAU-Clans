package me.trae.clans.fishing.modules.caught;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.FishingWeightEvent;
import me.trae.clans.fishing.events.PlayerCaughtFishEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMath;
import me.trae.core.utility.UtilServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleFishingCaughtWeight extends SpigotListener<Clans, FishingManager> {

    @ConfigInject(type = Integer.class, path = "Default-Min-Weight", defaultValue = "20")
    private int defaultMinWeight;

    @ConfigInject(type = Integer.class, path = "Default-Max-Weight", defaultValue = "200")
    private int defaultMaxWeight;

    @ConfigInject(type = Integer.class, path = "Big-Catch-Weight", defaultValue = "190")
    public int bigCatchWeight;

    public HandleFishingCaughtWeight(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFishCaught(final PlayerCaughtFishEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final FishingWeightEvent fishingWeightEvent = new FishingWeightEvent(event, UtilMath.getRandomNumber(Integer.class, this.defaultMinWeight, this.defaultMaxWeight));
        UtilServer.callEvent(fishingWeightEvent);
        if (!(fishingWeightEvent.hasWeight())) {
            return;
        }

        event.setWeight(fishingWeightEvent.getWeight());
    }
}