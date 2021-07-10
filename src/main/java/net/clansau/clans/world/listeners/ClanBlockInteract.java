package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilBlock;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClanBlockInteract extends CoreListener<WorldManager> {

    public ClanBlockInteract(final WorldManager manager) {
        super(manager, "Clan Block Interact");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockInteract(final PlayerInteractEvent e) {
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
        if (!(UtilBlock.isUsable(block.getType())) || block.getType().equals(Material.GLOWING_REDSTONE_ORE)) {
            return;
        }
        final Player player = e.getPlayer();
        final ClanManager clanManager = getManager().getClanManager();
        final Clan clan = clanManager.getClan(player.getUniqueId());
        final Clan land = clanManager.getClan(block.getLocation());
        if (this.canInteractBlock(block, clan, land)) {
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
        UtilMessage.message(player, "Clans", "You cannot use " + ChatColor.GREEN + UtilBlock.getCleanName(block) + ChatColor.GRAY + " in " + clanManager.getClanRelation(clan, land).getSuffix() + clanManager.getName(land, !(land instanceof AdminClan)) + ChatColor.GRAY + ".");
    }

    private boolean canInteractBlock(final Block block, final Clan clan, final Clan land) {
        if (land == null) {
            return true;
        }
        if (clan != null) {
            if (clan.equals(land)) {
                return true;
            }
            if (clan.isPillaging(land)) {
                return true;
            }
            if (clan.isTrusted(land)) {
                return block.getType().name().contains("DOOR") || block.getType().name().contains("BUTTON") || block.getType().name().contains("LEVER");
            }
        }
        return false;
    }
}