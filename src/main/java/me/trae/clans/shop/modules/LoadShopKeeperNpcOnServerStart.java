package me.trae.clans.shop.modules;

import me.trae.clans.Clans;
import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.npc.ShopNPC;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.server.events.ServerStartEvent;
import org.bukkit.event.EventHandler;

public class LoadShopKeeperNpcOnServerStart extends SpigotListener<Clans, ShopManager> {

    public LoadShopKeeperNpcOnServerStart(final ShopManager manager) {
        super(manager);
    }

    @EventHandler
    public void onServerStart(final ServerStartEvent event) {
        for (final ShopKeeper shopKeeper : this.getManager().getModulesByClass(ShopKeeper.class)) {
            for (final ShopNPC shopNPC : shopKeeper.getNpcList()) {
                shopNPC.spawn();
            }
        }
    }
}