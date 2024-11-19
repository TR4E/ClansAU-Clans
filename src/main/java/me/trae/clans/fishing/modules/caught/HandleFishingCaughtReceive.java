package me.trae.clans.fishing.modules.caught;

import me.trae.clans.Clans;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.fishing.events.PlayerCaughtFishEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.player.events.PlayerDisplayNameEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilVelocity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Arrays;
import java.util.Collections;

public class HandleFishingCaughtReceive extends SpigotListener<Clans, FishingManager> {

    public HandleFishingCaughtReceive(final FishingManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCaughtFish(final PlayerCaughtFishEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.isCaught())) {
            return;
        }

        final Player player = event.getPlayer();

        final Entity caught = event.getCaught();

        UtilVelocity.velocity(caught, UtilVelocity.getTrajectory(caught.getLocation().toVector(), player.getLocation().toVector()), 1.2D, 0.0D, 0.4D, 10.0D, false);

        String caughtName = event.getCaughtName();

        if (!(event.isCaughtFish())) {
            caughtName = String.format("a %s", caughtName);
        }

        if (!(event.isBroadcastInform())) {
            UtilMessage.simpleMessage(player, event.getInformPrefix(), "You caught <var>.", Collections.singletonList(caughtName));
        } else {
            for (final Player targetPlayer : UtilServer.getOnlinePlayers()) {
                final PlayerDisplayNameEvent playerDisplayNameEvent = new PlayerDisplayNameEvent(player, targetPlayer);
                UtilServer.callEvent(playerDisplayNameEvent);

                UtilMessage.simpleMessage(targetPlayer, event.getInformPrefix(), "<var> has caught <var>", Arrays.asList(playerDisplayNameEvent.getPlayerName(), caughtName));
            }
        }
    }
}