package me.trae.clans.shop.menus;

import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.menus.interfaces.IShopButton;
import me.trae.core.menu.Button;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public abstract class ShopButton extends Button<ShopMenu> implements IShopButton {

    public ShopButton(final ShopMenu menu, final ShopItem<?> shopItem) {
        super(menu, shopItem.getSlot(), shopItem.getItemBuilder());

        if (this.getBuilder().getItemStack().getItemMeta().hasLore()) {
            for (int i = 0; i < 2; i++) {
                getBuilder().addLore(" ");
            }
        }

        for (final String line : shopItem.getDescription()) {
            getBuilder().addLore(line);
        }
    }

    @Override
    public void onClick(final Player player, final ClickType clickType) {
        int amount = 1;

        if (clickType.isShiftClick()) {
            amount = 64;
        }

        final GamerManager gamerManager = this.getMenu().getInstance().getManagerByClass(GamerManager.class);
        final Gamer gamer = gamerManager.getGamerByPlayer(player);

        switch (clickType) {
            case LEFT:
            case SHIFT_LEFT: {
                this.buy(player, gamerManager, gamer, amount);
                break;
            }
            case RIGHT:
            case SHIFT_RIGHT: {
                this.sell(player, gamerManager, gamer, amount);
                break;
            }
        }
    }

    private void buy(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount) {
        final ShopItem<?> shopItem = this.getShopItem();

        if (!(shopItem.canBuy(player, gamerManager, gamer, amount))) {
            new SoundCreator(Sound.ITEM_BREAK, 1.0F, 0.6F).play(player);
            return;
        }

        shopItem.buy(player, gamerManager, gamer, amount);

        new SoundCreator(Sound.NOTE_PLING, 1.0F, 2.0F).play(player);
    }

    private void sell(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount) {
        final ShopItem<?> shopItem = this.getShopItem();

        if (!(shopItem.canSell(player, gamerManager, gamer, amount))) {
            new SoundCreator(Sound.ITEM_BREAK, 1.0F, 0.6F).play(player);
            return;
        }

        shopItem.sell(player, gamerManager, gamer, amount);

        new SoundCreator(Sound.NOTE_PLING, 1.0F, 2.0F).play(player);
    }
}