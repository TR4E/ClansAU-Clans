package me.trae.clans.world.menus;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.world.WorldManager;
import me.trae.clans.world.menus.buttons.*;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.menu.Menu;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class WarpMenu extends Menu<Clans, WorldManager> {

    public WarpMenu(final WorldManager manager, final Player player) {
        super(manager, player, InventoryType.DISPENSER, UtilColor.bold(ChatColor.GREEN) + "Travel Hub");
    }

    @Override
    public void fillPage(final Player player) {
        final ClanManager clanManager = this.getManager().getInstance().getManagerByClass(ClanManager.class);

        // Clan Home
        UtilJava.call(clanManager.getClanByPlayer(player), clan -> {
            if (clan != null && clan.hasHome()) {
                this.addButton(new ClanHomeWarpButton(this) {
                    @Override
                    public Location getLocation() {
                        return clan.getHome();
                    }
                });
            }
        });

        // Blue Spawn
        UtilJava.call(clanManager.getClanByName("Blue_Spawn"), clan -> {
            if (clan != null && clan.hasHome()) {
                this.addButton(new BlueSpawnWarpButton(this) {
                    @Override
                    public Location getLocation() {
                        return clan.getHome();
                    }
                });
            }
        });

        // Red Spawn
        UtilJava.call(clanManager.getClanByName("Red_Spawn"), clan -> {
            if (clan != null && clan.hasHome()) {
                this.addButton(new RedSpawnWarpButton(this) {
                    @Override
                    public Location getLocation() {
                        return clan.getHome();
                    }
                });
            }
        });

        // Blue Shops
        UtilJava.call(clanManager.getClanByName("Blue_Shops"), clan -> {
            if (clan != null && clan.hasHome()) {
                this.addButton(new BlueShopsWarpButton(this) {
                    @Override
                    public Location getLocation() {
                        return clan.getHome();
                    }
                });
            }
        });

        // Red Shops
        UtilJava.call(clanManager.getClanByName("Red_Shops"), clan -> {
            if (clan != null && clan.hasHome()) {
                this.addButton(new RedShopsWarpButton(this) {
                    @Override
                    public Location getLocation() {
                        return clan.getHome();
                    }
                });
            }
        });

        if (this.getManager().getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            // Fields
            UtilJava.call(clanManager.getClanByName("Fields"), clan -> {
                if (clan != null && clan.hasHome()) {
                    this.addButton(new FieldsWarpButton(this) {
                        @Override
                        public Location getLocation() {
                            return clan.getHome();
                        }
                    });
                }
            });

            // Lake
            UtilJava.call(new Location(UtilWorld.getDefaultWorld(), -68.5D, 68.0D, 29.5D, UtilLocation.getYawByDirectionType(DirectionType.NORTH), 0.0F), location -> {
                if (location != null) {
                    this.addButton(new LakeWarpButton(this) {
                        @Override
                        public Location getLocation() {
                            return location;
                        }
                    });
                }
            });
        }
    }
}