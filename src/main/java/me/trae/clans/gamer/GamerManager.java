package me.trae.clans.gamer;

import me.trae.clans.Clans;
import me.trae.core.gamer.abstracts.AbstractGamerManager;
import org.bukkit.entity.Player;

public class GamerManager extends AbstractGamerManager<Clans, Gamer> {

    public GamerManager(final Clans instance) {
        super(instance);
    }

    @Override
    public Gamer createGamer(final Player player) {
        return new Gamer(player);
    }
}