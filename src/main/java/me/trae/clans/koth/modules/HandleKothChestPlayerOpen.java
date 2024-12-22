package me.trae.clans.koth.modules;

import me.trae.clans.Clans;
import me.trae.clans.koth.Koth;
import me.trae.clans.koth.KothManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.player.events.PlayerDisplayNameEvent;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilTime;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public class HandleKothChestPlayerOpen extends SpigotListener<Clans, KothManager> {

    public HandleKothChestPlayerOpen(final KothManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (!(block.getState() instanceof Chest)) {
            return;
        }

        if (!(this.getManager().isKothChest(block))) {
            return;
        }

        final Player player = event.getPlayer();

        final Koth koth = this.getManager().getModuleByClass(Koth.class);
        if (koth == null) {
            return;
        }

        if (this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            return;
        }

        event.setCancelled(true);

        if (koth.isChestLocked()) {
            UtilMessage.simpleMessage(player, "KoTH", "The KoTH chest will unlock in <green><var></green>.", Collections.singletonList(UtilTime.getTime(UtilTime.getRemaining(koth.chestLockedSystemTime, koth.chestLockedDuration))));
            return;
        }

        if (!(koth.canOpenChest(player))) {
            if (!(koth.isChestOpener(player))) {
                koth.addChestOpener(player);
                UtilMessage.simpleMessage(player, "KoTH", "You must now wait <green><var></green>.", Collections.singletonList(UtilTime.getTime(koth.chestOpenedDuration)));
                return;
            }

            UtilMessage.simpleMessage(player, "KoTH", "You must wait <green><var></green>.", Collections.singletonList(UtilTime.getTime(UtilTime.getRemaining(koth.getChestOpenerMap().get(player.getUniqueId()), koth.chestOpenedDuration))));
            return;
        }

        this.openChest(player, UtilJava.cast(Chest.class, block.getState()));
    }

    private void openChest(final Player player, final Chest chest) {
        player.openInventory(chest.getInventory());

        for (final Player targetPlayer : UtilServer.getOnlinePlayers()) {
            final PlayerDisplayNameEvent playerDisplayNameEvent = new PlayerDisplayNameEvent(player, targetPlayer);
            UtilServer.callEvent(playerDisplayNameEvent);

            UtilMessage.simpleMessage(targetPlayer, "KoTH", "<var> has opened the chest!", Collections.singletonList(playerDisplayNameEvent.getPlayerName()));
        }
    }
}