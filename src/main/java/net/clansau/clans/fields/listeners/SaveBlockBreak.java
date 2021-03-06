package net.clansau.clans.fields.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.fields.Fields;
import net.clansau.clans.fields.FieldsManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class SaveBlockBreak extends CoreListener<FieldsManager> {

    public SaveBlockBreak(final FieldsManager manager) {
        super(manager, "Save Block Break");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Block block = e.getBlock();
        if (block == null) {
            return;
        }
        if (!(getManager().isFieldsBlock(block.getType()))) {
            return;
        }
        final Clan land = getInstance().getManager(ClanManager.class).getClan(block.getLocation());
        if (land == null) {
            return;
        }
        if (!(land instanceof AdminClan) && !(land.getName().equalsIgnoreCase("Fields"))) {
            return;
        }
        final Fields fields = getManager().getFieldsBlock(block.getLocation());
        if (fields == null) {
            return;
        }
        if (!(fields.getLocation().equals(block.getLocation()))) {
            return;
        }
        if (!(fields.getMaterial().equals(block.getType()) || block.getType().equals(Material.GLOWING_REDSTONE_ORE))) {
            return;
        }
        final Player player = e.getPlayer();
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(client.isAdministrating())) {
            return;
        }
        getManager().deleteSavedBlock(block);
        UtilMessage.message(player, "Fields", "Deleted Block " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + " at " + ChatColor.YELLOW + UtilLocation.locToString(block.getLocation()) + ChatColor.GRAY + ".");
    }
}