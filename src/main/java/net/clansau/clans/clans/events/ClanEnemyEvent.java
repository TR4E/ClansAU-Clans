package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.entity.Player;

public class ClanEnemyEvent extends CustomCancellableEvent {

    private final Player player;
    private final Clan clan, target;

    public ClanEnemyEvent(final Player player, final Clan clan, final Clan target) {
        this.player = player;
        this.clan = clan;
        this.target = target;
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
}