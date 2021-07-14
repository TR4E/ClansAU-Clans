package net.clansau.clans.farming.listeners;

import net.clansau.clans.farming.FarmingManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.Primitive;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class FarmingListener extends CoreListener<FarmingManager> {

    public FarmingListener(final FarmingManager manager) {
        super(manager, "Farming Listener");
        addPrimitive("Enabled", new Primitive<>(true));
        addPrimitive("MaxYLevel", new Primitive<>(60));
        addPrimitive("MinYLevel", new Primitive<>(44));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Block block = e.getBlock();
        if (block == null) {
            return;
        }
        if (!(getManager().isAgriculture(block.getType(), true))) {
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
        if (!(getPrimitiveCasted(Boolean.class, "Enabled"))) {
            e.setCancelled(true);
            UtilMessage.message(player, "Game", ChatColor.YELLOW + "Farming" + ChatColor.GRAY + " is currently not enabled.");
            return;
        }
        final int maxYLevel = getPrimitiveCasted(Integer.class, "MaxYLevel");
        final int minYLevel = getPrimitiveCasted(Integer.class, "MinYLevel");
        if (block.getLocation().getY() > minYLevel && block.getLocation().getY() < maxYLevel) {
            return;
        }
        e.setCancelled(true);
        UtilMessage.message(player, "Agriculture", "You can only cultivate between " + ChatColor.YELLOW + minYLevel + ChatColor.GRAY + " and " + ChatColor.YELLOW + maxYLevel + ChatColor.GRAY + " Y.");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }
        final Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        final Player player = e.getPlayer();
        if (player.getInventory().getItemInHand() == null) {
            return;
        }
        if (!(getManager().isFarming(player, block))) {
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            return;
        }
        if (!(getPrimitiveCasted(Boolean.class, "Enabled"))) {
            e.setCancelled(true);
            UtilMessage.message(player, "Game", ChatColor.YELLOW + "Farming" + ChatColor.GRAY + " is currently not enabled.");
            return;
        }
        final int maxYLevel = getPrimitiveCasted(Integer.class, "MaxYLevel");
        final int minYLevel = getPrimitiveCasted(Integer.class, "MinYLevel");
        if (block.getLocation().getY() > minYLevel && block.getLocation().getY() < maxYLevel) {
            return;
        }
        e.setCancelled(true);
        UtilMessage.message(player, "Agriculture", "You can only cultivate between " + ChatColor.YELLOW + minYLevel + ChatColor.GRAY + " and " + ChatColor.YELLOW + maxYLevel + ChatColor.GRAY + " Y.");
    }
}