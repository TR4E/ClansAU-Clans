package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClanHomeEvent extends CustomCancellableEvent {

    private final Player player;
    private final Clan clan;
    private final Location home;

    public ClanHomeEvent(final Player player, final Clan clan, final Location home) {
        this.player = player;
        this.clan = clan;
        this.home = home;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Clan getClan() {
        return this.clan;
    }

    public final Location getHome() {
        return this.home;
    }
}