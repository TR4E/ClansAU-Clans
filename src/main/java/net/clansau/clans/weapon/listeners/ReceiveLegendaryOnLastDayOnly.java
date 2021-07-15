package net.clansau.clans.weapon.listeners;

import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.ClanModule;
import net.clansau.clans.weapon.WeaponManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilMessage;
import net.clansau.core.weapon.events.LegendaryCommandEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ReceiveLegendaryOnLastDayOnly extends CoreListener<WeaponManager> {

    public ReceiveLegendaryOnLastDayOnly(final WeaponManager manager) {
        super(manager, "Receive Legendary On Last Day Only");
    }

    @EventHandler
    public void onLegendaryCommand(final LegendaryCommandEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (getInstance().getManager(ClanManager.class).getModule(ClanModule.class).getPrimitiveCasted(Boolean.class, "LastDay")) {
            return;
        }
        final Player player = e.getPlayer();
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            return;
        }
        e.setCancelled(true);
        UtilMessage.message(player, "Legendary", "You can only receive a Legendary on Last Day!");
    }
}