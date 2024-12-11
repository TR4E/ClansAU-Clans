package me.trae.clans.world.commands;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.item.ItemBuilder;
import me.trae.core.item.events.ItemUpdateEvent;
import me.trae.core.updater.annotations.Update;
import me.trae.core.updater.interfaces.Updater;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.UtilWorld;
import me.trae.core.vanish.VanishManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TrackCommand extends Command<Clans, WorldManager> implements PlayerCommandType, Updater, Listener {

    private final Map<Player, Player> TRACKER_MAP = new HashMap<>();

    private final ClientManager CLIENT_MANAGER;
    private final VanishManager VANISH_MANAGER;

    private final Material MATERIAL = Material.COMPASS;

    public TrackCommand(final WorldManager manager) {
        super(manager, "track", new String[]{"find", "locate"}, Rank.DEFAULT);

        this.CLIENT_MANAGER = this.getInstance(Core.class).getManagerByClass(ClientManager.class);
        this.VANISH_MANAGER = this.getInstance(Core.class).getManagerByClass(VanishManager.class);
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <player>";
    }

    @Override
    public String getDescription() {
        return "Track a Player with a Compass";
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        final ItemStack itemStack = player.getEquipment().getItemInHand();
        if (itemStack == null || itemStack.getType() != this.MATERIAL) {
            UtilMessage.simpleMessage(player, "Track", "You are not holding a <yellow><var></yellow>.", Collections.singletonList(UtilString.clean(this.MATERIAL.name())));
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Track", "You did not input a Player.");
            return;
        }

        final Player targetPlayer = UtilPlayer.searchPlayer(player, args[0], true);
        if (targetPlayer == null) {
            return;
        }

        if (!(this.canTrack(player, targetPlayer))) {
            return;
        }

        this.TRACKER_MAP.put(player, targetPlayer);

        UtilMessage.simpleMessage(player, "Track", "You are now tracking <yellow><var></yellow>.", Collections.singletonList(targetPlayer.getName()));
    }

    private boolean canTrack(final Player player, final Player targetPlayer) {
        if (player == targetPlayer) {
            UtilMessage.message(player, "Track", "You cannot track yourself!");
            return false;
        }

        if (player.getWorld() != targetPlayer.getWorld()) {
            UtilMessage.message(player, "Track", "You cannot track a player from a different world!");
            return false;
        }

        if (!(this.isValid(player, targetPlayer))) {
            UtilMessage.message(player, "Track", "You cannot track this player at this time!");
            return false;
        }

        return true;
    }

    private boolean isValid(final Player player, final Player targetPlayer) {
        if (this.CLIENT_MANAGER.getClientByPlayer(targetPlayer).isAdministrating()) {
            return false;
        }

        if (!(this.VANISH_MANAGER.canSeeByPlayer(targetPlayer, player))) {
            return false;
        }

        return true;
    }

    @Update(delay = 250L, asynchronous = true)
    public void onUpdater() {
        this.TRACKER_MAP.entrySet().removeIf(entry -> {
            final Player player = entry.getKey();
            final Player targetPlayer = entry.getValue();

            if (player == null || targetPlayer == null) {
                return true;
            }

            if (!(this.isValid(player, targetPlayer))) {
                player.setCompassTarget(UtilWorld.getSpawnLocation());
                return true;
            }

            player.setCompassTarget(targetPlayer.getLocation());
            return false;
        });
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        final ItemBuilder builder = event.getBuilder();

        if (builder.getItemStack().getType() != this.MATERIAL) {
            return;
        }

        builder.setDisplayName("Tracking Device");
        builder.addLore("Usage: <aqua>/track <player>");
    }
}