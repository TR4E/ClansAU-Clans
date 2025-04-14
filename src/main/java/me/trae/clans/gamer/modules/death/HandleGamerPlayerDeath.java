package me.trae.clans.gamer.modules.death;

import me.trae.api.death.events.CustomDeathEvent;
import me.trae.clans.Clans;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleGamerPlayerDeath extends SpigotListener<Clans, GamerManager> {

    public HandleGamerPlayerDeath(final GamerManager manager) {
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

        UtilJava.call(this.getManager().getGamerByPlayer(event.getEntityByClass(Player.class)), gamer -> {
            gamer.setDeaths(gamer.getDeaths() + 1);

            this.getManager().addDelayedSave(gamer, GamerProperty.DEATHS);
        });

        UtilJava.call(this.getManager().getGamerByPlayer(event.getKillerByClass(Player.class)), gamer -> {
            gamer.setKills(gamer.getKills() + 1);

            this.getManager().addDelayedSave(gamer, GamerProperty.KILLS);
        });
    }
}