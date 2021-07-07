package net.clansau.clans.clans.events;

import net.clansau.clans.clans.Clan;
import net.clansau.core.framework.event.CustomCancellableEvent;
import org.bukkit.entity.Player;

public class ClanChatEvent extends CustomCancellableEvent {

    private final Player player;
    private final Clan clan;
    private final String message;

    public ClanChatEvent(final Player player, final Clan clan, final String message) {
        this.player = player;
        this.clan = clan;
        this.message = message;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final Clan getClan() {
        return this.clan;
    }

    public final String getMessage() {
        return this.message;
    }
}