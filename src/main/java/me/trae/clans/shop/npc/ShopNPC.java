package me.trae.clans.shop.npc;

import me.trae.clans.Clans;
import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.menus.ShopMenu;
import me.trae.clans.shop.npc.interfaces.IShopNPC;
import me.trae.core.npc.CustomNPC;
import me.trae.core.npc.models.ClickableNPC;
import me.trae.core.utility.UtilMenu;
import me.trae.core.utility.UtilPlugin;
import me.trae.core.utility.enums.ClickType;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ShopNPC extends CustomNPC implements ClickableNPC, IShopNPC {

    private final ShopKeeper shopKeeper;

    public ShopNPC(final EntityType entityType, final Location location, final ShopKeeper shopKeeper) {
        super(entityType, location);

        this.shopKeeper = shopKeeper;
    }

    @Override
    public String getDisplayName() {
        return this.getShopKeeper().getDisplayName();
    }

    @Override
    public void onSpawn() {
        this.getNPC().getOrAddTrait(LookClose.class).lookClose(true);
    }

    @Override
    public void onClick(final Player player, final ClickType clickType) {
        if (clickType != ClickType.RIGHT) {
            return;
        }

        UtilMenu.open(new ShopMenu(UtilPlugin.getInstance(Clans.class).getManagerByClass(ShopManager.class), player, this.getShopKeeper()) {
            @Override
            public ShopKeeper getShopKeeper() {
                return ShopNPC.this.getShopKeeper();
            }
        });
    }

    @Override
    public ShopKeeper getShopKeeper() {
        return this.shopKeeper;
    }
}