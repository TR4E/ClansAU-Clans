package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.core.framework.blockrestore.BlockRestoreManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilBlock;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ExplosionListener extends CoreListener<ClanManager> {

    public ExplosionListener(final ClanManager manager) {
        super(manager, "Explosion Listener");
    }

    @EventHandler
    public void onBlockExplode(final EntityExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getEntity() instanceof TNTPrimed)) {
            return;
        }
        e.setCancelled(true);
        final Set<Clan> attacked = new HashSet<>();
        C:
        for (final Block block : UtilLocation.sphere(e.getLocation(), 4, 4, false, true, 2).stream().map(Location::getBlock).collect(Collectors.toList())) {
            if (block == null || block.getType().equals(Material.AIR)) {
                continue;
            }
            if (UtilBlock.isBlacklisted(block.getType()) || block.getType().equals(Material.BEACON)) {
                continue;
            }
            if (getInstance().getManager(BlockRestoreManager.class).isRestoreBlock(block)) {
                continue;
            }
            final Clan land = getManager().getClan(block.getLocation());
            if (land != null) {
                if (land instanceof AdminClan || land.isTNTProtected()) {
                    continue;
                }
                attacked.add(land);
            }
            if (block.isLiquid()) {
                block.setType(Material.AIR);
                continue;
            }
            for (final TNTBlocks tntBlock : TNTBlocks.values()) {
                if (block.getType().equals(tntBlock.getMaterial()) && block.getData() == tntBlock.getData()) {
                    block.setTypeIdAndData(tntBlock.getNewMaterial().getId(), tntBlock.getNewData(), true);
                    continue C;
                }
            }
            block.breakNaturally();
        }
        for (final Clan clan : attacked) {
            UtilMessage.broadcast(ChatColor.GRAY + "Saving Last TNTed " + ChatColor.YELLOW + "Clan " + clan.getName());
            clan.setLastTNTed(System.currentTimeMillis());
            getManager().getRepository().updateLastTNTed(clan);
            this.alertClan(clan, e);
        }
    }

    private void alertClan(final Clan target, final EntityExplodeEvent e) {
        final TNTPrimed tntPrimed = (TNTPrimed) e.getEntity();
        if (!(tntPrimed.getSource() instanceof Player)) {
            return;
        }
        final Player player = (Player) tntPrimed.getSource();
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            target.messageClan("Clans", "Your Territory is under attack by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".", null);
        } else {
            final ClanRelation relation = getManager().getClanRelation(target, clan);
            target.messageClan("Clans", "Your Territory is under attack by " + relation.getSuffix() + getManager().getName(clan, true) + ChatColor.GRAY + ".", null);
        }
    }

    public enum TNTBlocks {

        STONE_BRICK(Material.SMOOTH_BRICK, (byte) 0, Material.SMOOTH_BRICK, (byte) 2),
        NETHER_BRICK(Material.NETHER_BRICK, (byte) 0, Material.NETHERRACK, (byte) 0),
        QUARTZ_BLOCK(Material.QUARTZ_BLOCK, (byte) 0, Material.QUARTZ_BLOCK, (byte) 1),
        SANDSTONE(Material.SANDSTONE, (byte) 2, Material.SANDSTONE, (byte) 0),
        RED_SANDSTONE(Material.RED_SANDSTONE, (byte) 2, Material.RED_SANDSTONE, (byte) 0),
        PRISMARINE_01(Material.PRISMARINE, (byte) 2, Material.PRISMARINE, (byte) 1),
        PRISMARINE_02(Material.PRISMARINE, (byte) 1, Material.PRISMARINE, (byte) 0);

        private final Material material, newMaterial;
        private final byte data, newData;

        TNTBlocks(final Material material, final byte data, final Material newMaterial, final byte newData) {
            this.material = material;
            this.data = data;
            this.newMaterial = newMaterial;
            this.newData = newData;
        }

        public final Material getMaterial() {
            return this.material;
        }

        public final Material getNewMaterial() {
            return this.newMaterial;
        }

        public final byte getData() {
            return this.data;
        }

        public final byte getNewData() {
            return this.newData;
        }
    }
}