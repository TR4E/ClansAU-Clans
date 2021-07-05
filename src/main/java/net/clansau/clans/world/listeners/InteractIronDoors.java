package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.framework.recharge.RechargeManager;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractIronDoors extends CoreListener<WorldManager> {

    public InteractIronDoors(final WorldManager manager) {
        super(manager, "Interact Iron Doors");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDoorInteract(final PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        if (!(this.isDoor(block.getType()))) {
            return;
        }
        final Player player = e.getPlayer();
        if (!(getInstance().getManager(ClanManager.class).hasAccess(player, block.getLocation()))) {
            if (getInstance().getManager(RechargeManager.class).add(player, "Door Knock", 450L, false)) {
                block.getWorld().playEffect(block.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
            }
            return;
        }
        if (!(block.getType().equals(Material.IRON_TRAPDOOR)) && block.getData() >= 8) {
            block = block.getRelative(BlockFace.DOWN);
        }
        if (block.getData() < 4) {
            block.getWorld().playSound(block.getLocation(), Sound.DOOR_OPEN, 1.0F, 1.0F);
            block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 1);
            block.setData((byte) (block.getData() + 4), true);
        } else {
            block.getWorld().playSound(block.getLocation(), Sound.DOOR_CLOSE, 1.0F, 1.0F);
            block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 1);
            block.setData((byte) (block.getData() - 4), true);
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 0));
    }

    private boolean isDoor(final Material m) {
        return (m.equals(Material.IRON_DOOR) || m.equals(Material.IRON_DOOR_BLOCK) || m.equals(Material.IRON_TRAPDOOR));
    }

}