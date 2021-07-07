package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.entity.Player;

public class MemberLeaveEvent extends CustomCancellableEvent {

    private final Player player;
    private final Clan clan;

    public MemberLeaveEvent(final Player player, final Clan clan) {
        this.player = player;
        this.clan = clan;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Clan getClan() {
        return this.clan;
    }
}