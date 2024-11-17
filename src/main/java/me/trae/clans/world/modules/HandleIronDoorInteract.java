package me.trae.clans.world.modules;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.clans.world.events.DoorToggleEvent;
import me.trae.core.Core;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HandleIronDoorInteract extends SpigotListener<Clans, WorldManager> {

    @ConfigInject(type = Boolean.class, name = "Knocking", defaultValue = "true")
    private boolean knocking;

    @ConfigInject(type = Long.class, name = "Delay", defaultValue = "400")
    private long delay;

    public HandleIronDoorInteract(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (!(this.isDoor(block.getType()))) {
            return;
        }

        event.setCancelled(true);

        if (block.getType() != Material.IRON_TRAPDOOR && block.getData() >= 8) {
            block = block.getRelative(BlockFace.DOWN);
        }

        final Player player = event.getPlayer();

        final DoorToggleEvent doorToggleEvent = new DoorToggleEvent(block, player);
        UtilServer.callEvent(doorToggleEvent);
        if (doorToggleEvent.isCancelled()) {
            this.handleKnock(player, block);
            return;
        }

        if (doorToggleEvent.isOpened()) {
            doorToggleEvent.setOpen(false);

            new SoundCreator(Sound.DOOR_CLOSE).play(block.getLocation());
        } else {
            doorToggleEvent.setOpen(true);

            new SoundCreator(Sound.DOOR_OPEN).play(block.getLocation());
        }

        UtilJava.call(ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ANIMATION), (packetContainer) -> {
            packetContainer.getIntegers().write(0, player.getEntityId());
            packetContainer.getIntegers().write(0, 1);

            UtilPlayer.sendPacket(player, packetContainer);
        });
    }

    private boolean isDoor(final Material material) {
        switch (material) {
            case IRON_DOOR:
            case IRON_DOOR_BLOCK:
            case IRON_TRAPDOOR:
                return true;
        }

        return false;
    }

    private void handleKnock(final Player player, final Block block) {
        if (!(this.knocking)) {
            return;
        }

        if (!(this.getInstance(Core.class).getManagerByClass(RechargeManager.class).add(player, "Door Knock", this.delay, false))) {
            return;
        }

        block.getWorld().playEffect(block.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
    }
}