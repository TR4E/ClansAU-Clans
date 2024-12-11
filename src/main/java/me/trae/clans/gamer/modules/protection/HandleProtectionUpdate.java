package me.trae.clans.gamer.modules.protection;

import me.trae.clans.Clans;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.clans.gamer.events.ProtectionCheckEvent;
import me.trae.clans.gamer.events.ProtectionRemoveEvent;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public class HandleProtectionUpdate extends SpigotUpdater<Clans, GamerManager> implements Listener {

    private final Set<Gamer> PAUSED = new HashSet<>();

    private final long UPDATE_DURATION = 1_000L;
    private final long SAVE_DURATION = 60_000L;

    public HandleProtectionUpdate(final GamerManager manager) {
        super(manager);
    }

    @Update(delay = UPDATE_DURATION)
    public void onUpdate() {
        for (final Gamer gamer : this.getManager().getGamers().values()) {
            if (!(gamer.hasProtection())) {
                continue;
            }

            if (UtilServer.getEvent(new ProtectionCheckEvent(gamer)).isCancelled()) {
                if (!(PAUSED.contains(gamer))) {
                    PAUSED.add(gamer);

                    this.getManager().getRepository().updateData(gamer, GamerProperty.PROTECTION);
                }
                continue;
            }

            PAUSED.remove(gamer);

            gamer.takeProtection(this.UPDATE_DURATION);

            if (!(gamer.hasProtection())) {
                this.getManager().getRepository().updateData(gamer, GamerProperty.PROTECTION);

                UtilMessage.simpleMessage(gamer.getPlayer(), "Protection", "You no longer have protection!");

                UtilServer.callEvent(new ProtectionRemoveEvent(gamer));
            }
        }
    }

    @Update(delay = SAVE_DURATION)
    public void onSave() {
        for (final Gamer gamer : this.getManager().getGamers().values()) {
            this.updateProtection(gamer);
        }
    }

    private void updateProtection(final Gamer gamer) {
        if (!(gamer.hasProtection())) {
            return;
        }

        if (this.PAUSED.contains(gamer)) {
            return;
        }

        this.getManager().getRepository().updateData(gamer, GamerProperty.PROTECTION);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Gamer gamer = this.getManager().getGamerByPlayer(event.getPlayer());
        if (gamer == null) {
            return;
        }

        this.updateProtection(gamer);
    }
}