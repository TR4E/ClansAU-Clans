package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.entity.Player;

public class ClanDisbandEvent extends CustomCancellableEvent {

    private final Player player;
    private final Clan clan;
    private final DisbandCause cause;

    public ClanDisbandEvent(final Player player, final Clan clan, final DisbandCause cause) {
        this.player = player;
        this.clan = clan;
        this.cause = cause;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Clan getClan() {
        return this.clan;
    }

    public final DisbandCause getCause() {
        return this.cause;
    }

    public enum DisbandCause {
        PLAYER, ENERGY, FORCE
    }
}