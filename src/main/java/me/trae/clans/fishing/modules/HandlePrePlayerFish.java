package me.trae.clans.fishing.modules;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.CustomFishingEvent;
import me.trae.clans.fishing.events.enums.State;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

public class HandlePrePlayerFish extends SpigotListener<Clans, FishingManager> {

    public HandlePrePlayerFish(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFish(final PlayerFishEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.setExpToDrop(0);

        final Player player = event.getPlayer();

        final Entity caught = event.getCaught();

        if (caught instanceof LivingEntity) {
            caught.setVelocity(player.getLocation().getDirection().normalize().multiply(-1.0D));
            return;
        }

        final FishHook hook = event.getHook();
        if (hook == null) {
            return;
        }

        if (caught != null) {
            event.getCaught().remove();
        }

        State state = null;

        switch (event.getState()) {
            case FISHING:
                state = State.REEL_OUT;
                break;
            case CAUGHT_FISH:
            case CAUGHT_ENTITY:
                state = State.CAUGHT;
                break;
            case FAILED_ATTEMPT:
                if (UtilBlock.isInWater(hook.getLocation())) {
                    state = State.REEL_IN;
                } else {
                    state = State.FAILED;
                }
                break;
            case IN_GROUND:
                state = State.FAILED;
                break;
        }

        if (state == null) {
            return;
        }

        UtilServer.callEvent(new CustomFishingEvent(player, state, hook));
    }
}