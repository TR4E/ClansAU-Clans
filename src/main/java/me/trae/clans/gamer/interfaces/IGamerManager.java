package me.trae.clans.gamer.interfaces;

import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.enums.GamerProperty;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public interface IGamerManager {

    Map<Gamer, Set<GamerProperty>> getDelayedSaves();

    void addDelayedSave(final Gamer gamer, final GamerProperty property);

    void giveCoins(final Player player, final int coins);
}