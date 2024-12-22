package me.trae.clans.world.menus;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.world.WorldManager;
import me.trae.clans.world.enums.WarpType;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.menu.Menu;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilJava;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class WarpMenu extends Menu<Clans, WorldManager> {

    public WarpMenu(final WorldManager manager, final Player player) {
        super(manager, player, InventoryType.DISPENSER, UtilColor.bold(ChatColor.GREEN) + "Travel Hub");
    }

    @Override
    public void fillPage(final Player player) {
        final Client client = this.getManager().getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player);

        for (final WarpType warpType : WarpType.values()) {
            if (warpType.showForAdministrating() && !(client.isAdministrating())) {
                continue;
            }

            this.addButton(new WarpButton(this, warpType.getSlot(), warpType.getItemStack()) {
                @Override
                public String getDisplayName() {
                    return warpType.getDisplayName();
                }

                @Override
                public Location getLocation() {
                    return warpType.getLocation();
                }
            });
        }

        // Clan Home
        UtilJava.call(this.getManager().getInstance().getManagerByClass(ClanManager.class).getClanByPlayer(player), clan -> {
            if (clan != null && clan.hasHome()) {
                this.addButton(new WarpButton(this, 4, new ItemStack(Material.BED)) {
                    @Override
                    public String getDisplayName() {
                        return "<aqua>Clan Home";
                    }

                    @Override
                    public Location getLocation() {
                        return clan.getHome();
                    }
                });
            }
        });
    }
}