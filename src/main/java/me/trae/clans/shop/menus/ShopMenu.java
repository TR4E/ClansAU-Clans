package me.trae.clans.shop.menus;

import me.trae.clans.Clans;
import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.menus.interfaces.IShopMenu;
import me.trae.core.menu.Menu;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class ShopMenu extends Menu<Clans, ShopManager> implements IShopMenu {

    public ShopMenu(final ShopManager manager, final Player player, final ShopKeeper shopKeeper) {
        super(manager, player, 54, shopKeeper.getDisplayName());
    }

    @Override
    public void fillPage(final Player player) {
        final List<ShopItem> shopItemList = this.getShopKeeper().getSubModulesByClass(ShopItem.class);

        for (final ShopItem<?> shopItem : shopItemList) {
            addButton(new ShopButton(this, shopItem) {
                @Override
                public ShopItem<?> getShopItem() {
                    return shopItem;
                }
            });
        }
    }

    @Override
    public long getButtonClickDelay() {
        return 25L;
    }
}