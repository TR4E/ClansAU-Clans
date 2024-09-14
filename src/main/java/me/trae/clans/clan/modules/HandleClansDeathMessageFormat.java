package me.trae.clans.clan.modules;

import me.trae.api.death.events.CustomDeathMessageEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleClansDeathMessageFormat extends SpigotListener<Clans, ClanManager> {

    public HandleClansDeathMessageFormat(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCustomDeathMessage(final CustomDeathMessageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player target = event.getTarget();

        if (event.getDeathEvent().getEntity() instanceof Player) {
            event.setEntityName(this.getPlayerName(event.getDeathEvent().getEntityByClass(Player.class), target));
        }

        if (event.getDeathEvent().getKiller() instanceof Player) {
            event.setKillerName(this.getPlayerName(event.getDeathEvent().getKillerByClass(Player.class), target));
        }
    }

    private String getPlayerName(final Player player, final Player target) {
        final ClanRelation clanRelation = this.getManager().getClanRelationByPlayer(target, player);

        return clanRelation.getSuffix() + player.getName();
    }
}