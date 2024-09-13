package me.trae.clans.gamer;

import me.trae.clans.gamer.interfaces.IGamer;
import me.trae.core.gamer.abstracts.AbstractGamer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Gamer extends AbstractGamer implements IGamer {

    private int coins;

    public Gamer(final UUID uuid) {
        super(uuid);
    }

    public Gamer(final Player player) {
        this(player.getUniqueId());
    }

    @Override
    public int getCoins() {
        return this.coins;
    }

    @Override
    public void setCoins(final int coins) {
        this.coins = coins;
    }
}