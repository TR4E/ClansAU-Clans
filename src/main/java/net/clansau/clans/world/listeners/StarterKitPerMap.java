package net.clansau.clans.world.listeners;

import net.clansau.champions.classes.role.events.KitCommandEvent;
import net.clansau.champions.classes.role.events.KitReceiveEvent;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.gamer.Gamer;
import net.clansau.core.gamer.GamerManager;
import net.clansau.core.gamer.GamerRepository;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class StarterKitPerMap extends CoreListener<WorldManager> {

    public StarterKitPerMap(final WorldManager manager) {
        super(manager, "Starter Kit Per Map");
    }

    @EventHandler
    public void onKitCommand(final KitCommandEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final Gamer gamer = getInstance().getManager(GamerManager.class).getGamer(player.getUniqueId());
        if (gamer == null) {
            return;
        }
        if (!(gamer.hasClaimedKit())) {
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            return;
        }
        e.setCancelled(true);
        UtilMessage.message(player, "Kit", "You have already claimed a Kit this Season!");
    }

    @EventHandler
    public void onKitReceive(final KitReceiveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final Gamer gamer = getInstance().getManager(GamerManager.class).getGamer(player.getUniqueId());
        if (gamer == null) {
            return;
        }
        if (gamer.hasClaimedKit()) {
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            return;
        }
        gamer.setClaimedKit(true);
        getInstance().getManager(GamerRepository.class).updateClaimedKit(gamer);
    }
}