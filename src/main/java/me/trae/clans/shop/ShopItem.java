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
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ShopItem<M extends ShopKeeper> extends SpigotSubModule<Clans, M> implements IShopItem {

    private final ItemStack itemStack;

    public ShopItem(final M module, final ItemStack itemStack) {
        super(module);

        this.itemStack = UtilItem.updateItemStack(itemStack);
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return new ItemBuilder(this.getItemStack(), this.getDisplayName());
    }

    @Override
    public boolean canBuy(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount) {
        if (!(gamer.hasCoins(this.getBuyPriceByAmount(amount)))) {
            UtilMessage.simpleMessage(player, "Shop", "You insufficient funds to purchase <green><var>x</green> of <green><var></green>.", Arrays.asList(String.valueOf(amount), this.getDisplayNameStripped()));
            return false;
        }

        return true;
    }

    @Override
    public boolean canSell(final Player player, final GamerManager gamerManager, final Gamer gamer, final int amount) {
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

        final ItemStack itemStack = this.getItemStack().clone();

        itemStack.setAmount(amount);
        UtilItem.insert(player, itemStack);
        UtilServer.callEvent(new ScoreboardUpdateEvent(player));

        UtilMessage.simpleMessage(player, "Shop", "You have purchased <green><var>x</green> of <green><var></green> for <gold><var></gold>.", Arrays.asList(String.valueOf(amount), this.getDisplayNameStripped(), this.getBuyPriceString(amount)));
    }

    @Override
    public void sell(final Player player, final GamerManager gamerManager, final Gamer gamer, int amount) {
        if (amount == 64) {
            amount = Math.min(64, UtilItem.getAmount(player, this.getItemStack()));
        }

        final int sellPrice = this.getSellPriceByAmount(amount);

        gamer.setCoins(gamer.getCoins() + sellPrice);
        gamerManager.getRepository().updateData(gamer, GamerProperty.COINS);
        UtilServer.callEvent(new ScoreboardUpdateEvent(player));

        UtilItem.remove(player, this.getItemStack(), amount);

        UtilMessage.simpleMessage(player, "Shop", "You have sold <green><var>x</green> of <green><var></green> for <gold><var></gold>.", Arrays.asList(String.valueOf(amount), this.getDisplayNameStripped(), this.getSellPriceString(amount)));
    }

    @Override
    public String[] getDescription() {
        final List<String> list = new ArrayList<>();

        list.add(UtilString.pair("<gray>Buy Price", this.getBuyPriceString(1)));
        list.add(UtilString.pair("<gray>Sell Price", this.getSellPriceString(1)));

        if (this.canStack()) {
            list.add(" ");
            list.add(UtilString.pair("<gray>Buy 64x Price", this.getBuyPriceString(64)));
            list.add(UtilString.pair("<gray>Sell 64x Price", this.getSellPriceString(64)));
        }

        return list.toArray(new String[0]);
    }
}