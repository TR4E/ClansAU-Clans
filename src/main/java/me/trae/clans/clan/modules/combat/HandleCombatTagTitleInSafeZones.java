package me.trae.clans.clan.modules.combat;

import me.trae.api.combat.Combat;
import me.trae.api.combat.events.CombatUpdaterEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.UtilTitle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HandleCombatTagTitleInSafeZones extends SpigotListener<Clans, ClanManager> {

    public HandleCombatTagTitleInSafeZones(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onCombatUpdater(final CombatUpdaterEvent event) {
        final Player player = event.getPlayer();

        if (!(this.getManager().isSafeByLocation(player.getLocation()))) {
            return;
        }

        final Combat combat = event.getCombat();

        if (combat.hasExpired()) {
            UtilTitle.sendTitle(player, " ", "<green>Safe", true, 2000L);
            return;
        }

        UtilTitle.sendTitle(player, " ", UtilString.format("<red>Unsafe for <green>%s", combat.getRemainingString()), false, 1000L);
    }
}