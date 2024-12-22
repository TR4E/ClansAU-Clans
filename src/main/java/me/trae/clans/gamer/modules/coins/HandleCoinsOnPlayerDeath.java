package me.trae.clans.gamer.modules.coins;

import me.trae.api.champions.role.events.RoleCheckEvent;
import me.trae.api.death.events.CustomDeathEvent;
import me.trae.clans.Clans;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.player.events.PlayerDisplayNameEvent;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Arrays;

public class HandleCoinsOnPlayerDeath extends SpigotListener<Clans, GamerManager> {

    @ConfigInject(type = Integer.class, path = "Coins-Percentage", defaultValue = "10")
    private int coinsPercentage;

    @ConfigInject(type = Boolean.class, path = "Class-Required", defaultValue = "false")
    private boolean classRequired;

    public HandleCoinsOnPlayerDeath(final GamerManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomDeath(final CustomDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getKiller() instanceof Player)) {
            return;
        }

        if (event.getEntity().equals(event.getKiller())) {
            return;
        }

        final Player player = event.getEntityByClass(Player.class);

        if (this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            return;
        }

        if (this.classRequired && UtilServer.getEvent(new RoleCheckEvent(player)).getRole() == null) {
            return;
        }

        final Player killer = event.getKillerByClass(Player.class);

        final Gamer playerGamer = this.getManager().getGamerByPlayer(player);
        final Gamer killerGamer = this.getManager().getGamerByPlayer(killer);

        final int coins = (int) (playerGamer.getCoins() * (this.coinsPercentage * 0.01));

        if (coins <= 0) {
            return;
        }

        playerGamer.setCoins(playerGamer.getCoins() - coins);
        this.getManager().getRepository().updateData(playerGamer, GamerProperty.COINS);
        UtilServer.callEvent(new ScoreboardUpdateEvent(player));

        killerGamer.setCoins(killerGamer.getCoins() + coins);
        this.getManager().getRepository().updateData(killerGamer, GamerProperty.COINS);
        UtilServer.callEvent(new ScoreboardUpdateEvent(killer));

        UtilMessage.simpleMessage(player, "Coins", "You lost <gold><var></gold> to <var>.", Arrays.asList(UtilString.toDollar(coins), UtilServer.getEvent(new PlayerDisplayNameEvent(killer, player)).getPlayerName()));
        UtilMessage.simpleMessage(killer, "Coins", "You stole <gold><var></gold> from <var>.", Arrays.asList(UtilString.toDollar(coins), UtilServer.getEvent(new PlayerDisplayNameEvent(player, killer)).getPlayerName()));
    }
}