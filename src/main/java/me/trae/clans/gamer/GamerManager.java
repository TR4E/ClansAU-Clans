package me.trae.clans.gamer;

import me.trae.clans.Clans;
import me.trae.clans.gamer.commands.EconomyCommand;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.clans.gamer.interfaces.IGamerManager;
import me.trae.clans.gamer.modules.HandleCoinsOnEntityDeath;
import me.trae.clans.gamer.modules.HandleCoinsOnPlayerDeath;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.gamer.abstracts.AbstractGamerManager;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.entity.Player;

public class GamerManager extends AbstractGamerManager<Clans, Gamer, GamerProperty, GamerRepository> implements IGamerManager {

    @ConfigInject(type = Integer.class, path = "Starter-Coins-Amount", defaultValue = "5000")
    public int starterCoinsAmount;

    public GamerManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        super.registerModules();

        // Commands
        addModule(new EconomyCommand(this));

        // Modules
        addModule(new HandleCoinsOnEntityDeath(this));
        addModule(new HandleCoinsOnPlayerDeath(this));
    }

    @Override
    public Class<GamerRepository> getClassOfRepository() {
        return GamerRepository.class;
    }

    @Override
    public Gamer createGamer(final Player player) {
        return new Gamer(player);
    }

    @Override
    public Gamer createGamer(final EnumData<GamerProperty> data) {
        return new Gamer(data);
    }

    @Override
    public void giveCoins(final Player player, final int coins) {
        final Gamer gamer = this.getGamerByPlayer(player);
        if (gamer == null) {
            return;
        }

        gamer.setCoins(gamer.getCoins() + coins);
        this.getRepository().updateData(gamer, GamerProperty.COINS);

        UtilServer.callEvent(new ScoreboardUpdateEvent(player));
    }
}