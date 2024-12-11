package me.trae.clans.shop.commands;

import me.trae.clans.Clans;
import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.menus.ShopMenu;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PlayerCommandType;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMenu;
import org.bukkit.entity.Player;

public class ShopCommand extends Command<Clans, ShopManager> implements PlayerCommandType {

    public ShopCommand(final ShopManager manager) {
        super(manager, "shop", new String[0], Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        ShopKeeper shopKeeper = this.getManager().getModuleByClass(ResourcesShopKeeper.class);

        if (args.length == 1) {
            try {
                shopKeeper = UtilJava.cast(ShopKeeper.class, this.getManager().getModuleByName(args[0]));
                if (shopKeeper == null) {
                    return;
                }
            } catch (final Exception ignored) {
            }
        }

        final ShopKeeper finalShopKeeper = shopKeeper;
        UtilMenu.open(new ShopMenu(this.getManager(), player, finalShopKeeper) {
            @Override
            public ShopKeeper getShopKeeper() {
                return finalShopKeeper;
            }
        });
    }
}