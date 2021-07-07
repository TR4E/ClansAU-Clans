package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.entity.Player;

public class ClanJoinEvent extends CustomCancellableEvent {

    private final Player player;
    private final Clan clan;
    private final boolean forced;

    public ClanJoinEvent(final Player player, final Clan clan, final boolean forced) {
        this.player = player;
        this.clan = clan;
        this.forced = forced;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Clan getClan() {
        return this.clan;
    }

    public final boolean isForced() {
        return this.forced;
    }

}