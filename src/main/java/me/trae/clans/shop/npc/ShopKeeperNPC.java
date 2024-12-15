package me.trae.clans.shop.npc;

import me.trae.clans.Clans;
import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.menus.ShopMenu;
import me.trae.clans.shop.npc.interfaces.IShopKeeperNPC;
import me.trae.core.npc.CustomNPC;
import me.trae.core.npc.models.ClickableNPC;
import me.trae.core.npc.models.LookCloseNPC;
import me.trae.core.npc.models.SilentSoundNPC;
import me.trae.core.utility.UtilMenu;
import me.trae.core.utility.UtilPlugin;
import me.trae.core.utility.enums.ClickType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ShopKeeperNPC extends CustomNPC implements ClickableNPC, LookCloseNPC, SilentSoundNPC, IShopKeeperNPC {

    private final ShopKeeper shopKeeper;

    public ShopKeeperNPC(final EntityType entityType, final Location location, final ShopKeeper shopKeeper) {
        super(entityType, location);

        this.shopKeeper = shopKeeper;
    }

    @Override
    public String getDisplayName() {
        return this.getShopKeeper().getDisplayName();
    }

    @Override
    public void onClick(final Player player, final ClickType clickType) {
        if (clickType != ClickType.RIGHT) {
            return;
        }

        UtilMenu.open(new ShopMenu(UtilPlugin.getInstance(Clans.class).getManagerByClass(ShopManager.class), player, this.getShopKeeper()) {
            @Override
            public ShopKeeper getShopKeeper() {
                return ShopKeeperNPC.this.getShopKeeper();
            }
        });
    }

    @Override
    public ShopKeeper getShopKeeper() {
        return this.shopKeeper;
    }
}