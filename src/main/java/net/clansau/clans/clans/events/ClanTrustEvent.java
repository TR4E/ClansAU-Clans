package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.entity.Player;

public class ClanTrustEvent extends CustomCancellableEvent {

    private final Player player;
    private final Clan clan, target;
    private final boolean forced;

    public ClanTrustEvent(final Player player, final Clan clan, final Clan target, final boolean forced) {
        this.player = player;
        this.clan = clan;
        this.target = target;
        this.forced = forced;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Clan getClan() {
        return this.clan;
    }

    public final Clan getTarget() {
        return this.target;
    }

    public final boolean isForced() {
        return this.forced;
    }
}