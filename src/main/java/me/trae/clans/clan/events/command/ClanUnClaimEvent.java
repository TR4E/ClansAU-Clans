package me.trae.clans.clan.events.command;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.events.abstracts.types.ClanPlayerCancellableEvent;
import me.trae.core.client.Client;
import me.trae.core.event.types.IChunkEvent;
import me.trae.core.event.types.ITargetEvent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ClanUnClaimEvent extends ClanPlayerCancellableEvent implements IChunkEvent, ITargetEvent<Clan> {

    private final Chunk chunk;
    private final Clan target;

    public ClanUnClaimEvent(final Clan clan, final Player player, final Client client, final Chunk chunk, final Clan target) {
        super(clan, player, client);

        this.chunk = chunk;
        this.target = target;
    }

    @Override
    public Chunk getChunk() {
        return this.chunk;
    }

    @Override
    public Clan getTarget() {
        return this.target;
    }
}