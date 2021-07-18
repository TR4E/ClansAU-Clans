package net.clansau.clans.fields.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.fields.Fields;
import net.clansau.clans.fields.FieldsManager;
import net.clansau.clans.fields.FieldsModule;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilItem;
import net.clansau.core.utility.UtilMath;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.util.ArrayList;
import java.util.List;

public class GameBlockBreak extends CoreListener<FieldsManager> {

    public GameBlockBreak(final FieldsManager manager) {
        super(manager, "Game Block Break");
    }

    @EventHandler
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
        if (!(getManager().getModule(FieldsModule.class).isEnabled())) {
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
        if (client.isAdministrating()) {
            return;
        }
        e.setCancelled(true);
        getManager().addFieldsBlock(block, System.currentTimeMillis());
        this.handleDrops(block);
        if (block.getType().equals(Material.ENDER_CHEST)) {
            block.setType(Material.AIR);
        } else {
            block.setType(Material.BEDROCK);
        }
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        final ItemStack hand = player.getInventory().getItemInHand();
        if (hand == null || !(UtilItem.isPickaxe(hand.getType()))) {
            return;
        }
        hand.setDurability((short) (hand.getDurability() + 1));
    }

    private void handleDrops(final Block block) {
        final List<ItemStack> drops = new ArrayList<>();
        switch (block.getType()) {
            case LAPIS_ORE: {
                final Dye dye = new Dye();
                dye.setColor(DyeColor.BLUE);
                drops.add(dye.toItemStack(3));
                break;
            }
            case ENDER_CHEST: {
                final Material[] random = new Material[]{Material.GOLD_SWORD, Material.IRON_PICKAXE, Material.DIAMOND_SPADE};
                drops.add(new ItemStack(random[UtilMath.randomInt(random.length - 1)]));
                drops.add(new ItemStack(Material.EMERALD, UtilMath.randomInt(5)));
                drops.add(new ItemStack(Material.LEATHER, UtilMath.randomInt(10)));
                break;
            }
            case IRON_ORE: {
                drops.add(new ItemStack(Material.IRON_INGOT, 1));
                break;
            }
            case GOLD_ORE: {
                drops.add(new ItemStack(Material.GOLD_INGOT, 1));
                break;
            }
            default: {
                drops.addAll(block.getDrops());
                break;
            }
        }
        for (final ItemStack drop : drops) {
            block.getWorld().dropItem(block.getLocation().add(0.5D, 0.5D, 0.5D), drop);
        }
    }
}