package me.trae.clans.gamer.modules;

import me.trae.clans.Clans;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.updater.annotations.Update;
import me.trae.core.updater.interfaces.Updater;
import me.trae.core.utility.UtilMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class HandleGamerDelayedSaves extends SpigotListener<Clans, GamerManager> implements Updater {

    public HandleGamerDelayedSaves(final GamerManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Gamer gamer = this.getManager().getGamerByPlayer(event.getPlayer());

        if (!(this.getManager().getDelayedSaves().containsKey(gamer))) {
            return;
        }

        for (final GamerProperty gamerProperty : this.getManager().getDelayedSaves().remove(gamer)) {
            this.getManager().getRepository().updateData(gamer, gamerProperty);
        }
    }

    @Update(delay = 300_000L)
    public void onUpdater() {
        final AtomicBoolean updated = new AtomicBoolean(false);

        this.getManager().getDelayedSaves().entrySet().removeIf(entry -> {
            final Gamer gamer = entry.getKey();

            for (final GamerProperty gamerProperty : entry.getValue()) {
                this.getManager().getRepository().updateData(gamer, gamerProperty);
            }

            updated.set(true);

            return true;
        });

        if (updated.get()) {
            UtilMessage.log("Gamer", "Updated Delayed Saves!");
        }
    }
}