package me.trae.clans.crate.loot.models;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.Loot;
import me.trae.clans.crate.loot.models.interfaces.ICoinsLoot;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class CoinsLoot extends Loot implements ICoinsLoot {

    private final int coins;

    public CoinsLoot(final Crate module, final double chance, final ItemStack itemStack, final int coins) {
        super(module, chance, itemStack);

        this.coins = coins;
    }

    @Override
    public int getCoins() {
        return this.coins;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD + UtilString.toDollar(this.getCoins());
    }

    @Override
    public Consumer<Player> getConsumer() {
        return (player -> {
            final GamerManager gamerManager = this.getInstance().getManagerByClass(GamerManager.class);

            final Gamer gamer = gamerManager.getGamerByPlayer(player);
            if (gamer == null) {
                return;
            }

            gamer.setCoins(gamer.getCoins() + this.getCoins());

            gamerManager.getRepository().updateData(gamer, GamerProperty.COINS);

            UtilServer.callEvent(new ScoreboardUpdateEvent(player));
        });
    }
}