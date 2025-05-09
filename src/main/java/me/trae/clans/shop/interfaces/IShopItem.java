package me.trae.clans.shop.interfaces;

import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.core.item.ItemBuilder;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IShopItem {

    ItemStack getItemStack();

    int getSlot();

    int getBuyPrice();

    default int getBuyPriceByAmount(final int amount) {
        return this.getBuyPrice() * amount;
    }

    default boolean hasBuyPrice() {
        return this.getBuyPrice() > 0;
    }

    default String getBuyPriceString(final int amount) {
        return UtilString.format("<gold>%s", UtilString.toDollar(this.getBuyPriceByAmount(amount)));
    }

    int getSellPrice();

    default int getSellPriceByAmount(final int amount) {
        return this.getSellPrice() * amount;
    }

    default boolean hasSellPrice() {
        return this.getSellPrice() > 0;
    }

    default String getSellPriceString(final int amount) {
        return UtilString.format("<gold>%s", UtilString.toDollar(this.getSellPriceByAmount(amount)));
    }

    default boolean canStack() {
        return true;
    }

    ItemBuilder getItemBuilder();

    ItemBuilder toItemBuilder();

    boolean canBuy(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount);

    boolean canSell(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount);

    void buy(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount);

    void sell(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount);

    default String getDisplayName() {
        return UtilItem.getDisplayName(this.getItemStack(), false);
    }

    default String getDisplayNameStripped() {
        return ChatColor.stripColor(this.getDisplayName());
    }

    String[] getDescription();

    default boolean isSimilarWithItemMeta() {
        return true;
    }
}