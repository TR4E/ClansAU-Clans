package me.trae.clans.gamer.modules.coins;

import me.trae.clans.Clans;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import org.bukkit.entity.Player;

import java.util.Collections;

public class HandleOnlineRewardCoins extends SpigotUpdater<Clans, GamerManager> {

    @ConfigInject(type = Integer.class, path = "Amount", defaultValue = "2_000")
    private int amount;

    public HandleOnlineRewardCoins(final GamerManager manager) {
        super(manager);
    }

    @Update(delay = 3_600_000L)
    public void onUpdater() {
        for (final Gamer gamer : this.getManager().getGamers().values()) {
            final Player player = gamer.getPlayer();

            gamer.setCoins(gamer.getCoins() + this.amount);
            this.getManager().getRepository().updateData(gamer, GamerProperty.COINS);
            UtilServer.callEvent(new ScoreboardUpdateEvent(player));

            UtilMessage.simpleMessage(player, "Online Reward", "You received <gold><var></gold>.", Collections.singletonList(UtilString.toDollar(this.amount)));
        }
    }
}