package me.trae.clans.clan.modules.champions;

import me.trae.api.champions.role.events.KitReceiveEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;

public class HandleOverpoweredKits extends SpigotListener<Clans, ClanManager> {

    public HandleOverpoweredKits(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onKitReceive(final KitReceiveEvent event) {
        if (this.getManager().eotw) {
            event.setOverpowered(true);
        }
    }
}