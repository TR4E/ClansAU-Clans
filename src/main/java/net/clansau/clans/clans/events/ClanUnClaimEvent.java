package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ClanUnClaimEvent extends CustomCancellableEvent {

    private final Player player;
    private final Clan clan, land;
    private final Chunk chunk;

    public ClanUnClaimEvent(final Player player, final Clan clan, final Clan land, final Chunk chunk) {
        this.player = player;
        this.clan = clan;
        this.land = land;
        this.chunk = chunk;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Clan getClan() {
        return this.clan;
    }

    public final Clan getLand() {
        return this.land;
    }

    public final Chunk getChunk() {
        return this.chunk;
    }
}