package me.trae.clans.shop;

import me.trae.clans.Clans;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.clans.shop.interfaces.IShopItem;
import me.trae.core.framework.SpigotSubModule;
import me.trae.core.item.ItemBuilder;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ShopItem<M extends ShopKeeper> extends SpigotSubModule<Clans, M> implements IShopItem {

    private final ItemStack itemStack;

    public ShopItem(final M module, final ItemStack itemStack) {
        super(module);

        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return new ItemBuilder(UtilItem.updateItemStack(this.getItemStack()), this.getDisplayName());
    }

    @Override
    public ItemBuilder toItemBuilder() {
        return this.getItemBuilder();
    }

    @Override
    public boolean canBuy(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount) {
        if (!(this.hasBuyPrice())) {
            UtilMessage.message(player, "Shop", "You cannot buy this item!");
            return false;
        }

        if (!(gamer.hasCoins(this.getBuyPriceByAmount(amount)))) {
            UtilMessage.simpleMessage(player, "Shop", "You have insufficient funds to purchase <green><var>x</green> of <green><var></green>.", Arrays.asList(String.valueOf(amount), this.getDisplayNameStripped()));
            return false;
        }

        return true;
    }

    @Override
    public boolean canSell(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount) {
        if (!(this.hasSellPrice())) {
            UtilMessage.message(player, "Shop", "You cannot sell this item!");
            return false;
        }

        if (!(UtilItem.contains(player, this.getItemStack(), 1))) {
            UtilMessage.simpleMessage(player, "Shop", "You do not own <green><var>x</green> of <green><var></green> to sell.", Arrays.asList(String.valueOf(amount), this.getDisplayNameStripped()));
            return false;
        }

        return true;
    }

    @Override
    public void buy(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount) {
        gamer.setCoins(gamer.getCoins() - this.getBuyPriceByAmount(amount));
        gamerManager.getRepository().updateData(gamer, GamerProperty.COINS);

        UtilItem.insert(player, this.toItemBuilder().toItemStack(amount));
        UtilServer.callEvent(new ScoreboardUpdateEvent(player));

        UtilMessage.simpleMessage(player, "Shop", "You have purchased <green><var>x</green> of <green><var></green> for <gold><var></gold>.", Arrays.asList(String.valueOf(amount), this.getDisplayNameStripped(), this.getBuyPriceString(amount)));
    }

    @Override
    public void sell(final Player player, final GamerManager gamerManager, final Gamer gamer, int amount) {
        final ItemStack itemStack = this.toItemBuilder().toItemStack();

        if (amount == 64) {
            amount = Math.min(64, UtilItem.getAmount(player, itemStack));
        }

        final int sellPrice = this.getSellPriceByAmount(amount);

        gamer.setCoins(gamer.getCoins() + sellPrice);
        gamerManager.getRepository().updateData(gamer, GamerProperty.COINS);
        UtilServer.callEvent(new ScoreboardUpdateEvent(player));

        UtilItem.remove(player, itemStack, amount);

        UtilMessage.simpleMessage(player, "Shop", "You have sold <green><var>x</green> of <green><var></green> for <gold><var></gold>.", Arrays.asList(String.valueOf(amount), this.getDisplayNameStripped(), this.getSellPriceString(amount)));
    }

    @Override
    public String[] getDescription() {
        final List<String> list = new ArrayList<>();

        if (this.hasBuyPrice()) {
            list.add(UtilString.pair("<gray>Buy Price", this.getBuyPriceString(1)));
        }

        if (this.hasSellPrice()) {
            list.add(UtilString.pair("<gray>Sell Price", this.getSellPriceString(1)));
        }

        if (this.canStack()) {
            list.add(" ");
            if (this.hasBuyPrice()) {
                list.add(UtilString.pair("<gray>Buy 64x Price", this.getBuyPriceString(64)));
            }

            if (this.hasSellPrice()) {
                list.add(UtilString.pair("<gray>Sell 64x Price", this.getSellPriceString(64)));
            }
        }

        return list.toArray(new String[0]);
    }
}