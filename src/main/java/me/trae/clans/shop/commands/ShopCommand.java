package me.trae.clans.shop.commands;

import me.trae.clans.Clans;
import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.menus.ShopMenu;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMenu;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class ShopCommand extends Command<Clans, ShopManager> implements PlayerCommandType {

    public ShopCommand(final ShopManager manager) {
        super(manager, "shop", new String[0], Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        if (args.length == 0) {
            UtilMessage.message(player, "Shop", "You did not input a Shop Keeper.");
            return;
        }

        final ShopKeeper shopKeeper = this.getManager().searchShopKeeper(player, args[0], true);
        if (shopKeeper == null) {
            return;
        }

        UtilMenu.open(new ShopMenu(this.getManager(), player, shopKeeper) {
            @Override
            public ShopKeeper getShopKeeper() {
                return shopKeeper;
            }
        });
    }
}