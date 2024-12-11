package me.trae.clans.gamer.modules.coins;

import me.trae.api.champions.role.events.RoleCheckEvent;
import me.trae.api.death.events.CustomDeathEvent;
import me.trae.clans.Clans;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Arrays;

public class HandleCoinsOnEntityDeath extends SpigotListener<Clans, GamerManager> {

    @ConfigInject(type = Integer.class, path = "Multiplier", defaultValue = "20")
    private int multiplier;

    @ConfigInject(type = Boolean.class, path = "Class-Required", defaultValue = "false")
    private boolean classRequired;

    public HandleCoinsOnEntityDeath(final GamerManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCustomDeath(final CustomDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }

        if (!(event.getKiller() instanceof Player)) {
            return;
        }

        if (event.getEntity().equals(event.getKiller())) {
            return;
        }

        final LivingEntity entity = event.getEntityByClass(LivingEntity.class);

        if (UtilNPC.isNPC(entity)) {
            return;
        }

        final Player killer = event.getKillerByClass(Player.class);

        if (this.classRequired && UtilServer.getEvent(new RoleCheckEvent(killer)).getRole() == null) {
            return;
        }

        final Gamer killerGamer = this.getManager().getGamerByPlayer(killer);

        final int scale = (int) entity.getMaxHealth() * this.multiplier;

        final int coins = UtilMath.getRandomNumber(Integer.class, scale / 2, scale);

        if (coins <= 0) {
            return;
        }

        killerGamer.setCoins(killerGamer.getCoins() + coins);
        this.getManager().getRepository().updateData(killerGamer, GamerProperty.COINS);
        UtilServer.callEvent(new ScoreboardUpdateEvent(killer));

        UtilMessage.simpleMessage(killer, "Coins", "You gained <gold><var></gold> from killing a <yellow><var></yellow>.", Arrays.asList(UtilString.toDollar(coins), entity.getName()));
    }
}