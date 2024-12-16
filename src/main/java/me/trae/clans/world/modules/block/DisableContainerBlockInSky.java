package me.trae.clans.world.modules.block;

import me.trae.clans.Clans;
import me.trae.clans.tnt.events.TNTExplodeEvent;
import me.trae.clans.world.WorldManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class DisableContainerBlockInSky extends SpigotListener<Clans, WorldManager> {

    @ConfigInject(type = Integer.class, path = "Y-LeveL", defaultValue = "200")
    private int yLeveL;

    public DisableContainerBlockInSky(final WorldManager manager) {
        super(manager);
    }

    private boolean isValid(final Block block, final Player player) {
        if (block == null) {
            return false;
        }

        if (!(UtilBlock.isContainer(block.getType()))) {
            return false;
        }

        if (block.getLocation().getBlockY() < this.yLeveL) {
            return false;
        }

        if (player != null) {
            if (UtilPlugin.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
                return false;
            }
        }

        return true;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();
        final Player player = event.getPlayer();

        if (!(this.isValid(block, player))) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.simpleMessage(player, "Game", "You cannot break <yellow><var></yellow> above <green><var></green> Y Level.", Arrays.asList(UtilBlock.getDisplayName(block), String.valueOf(this.yLeveL)));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();
        final Player player = event.getPlayer();

        if (!(this.isValid(block, player))) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.simpleMessage(player, "Game", "You cannot place <yellow><var></yellow> above <green><var></green> Y Level.", Arrays.asList(UtilBlock.getDisplayName(block), String.valueOf(this.yLeveL)));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();
        final Player player = event.getPlayer();

        if (!(this.isValid(block, player))) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.simpleMessage(player, "Game", "You cannot use <yellow><var></yellow> above <green><var></green> Y Level.", Arrays.asList(UtilBlock.getDisplayName(block), String.valueOf(this.yLeveL)));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTNTExplode(final TNTExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.getBlocks().removeIf(block -> this.isValid(block, null));
    }
}