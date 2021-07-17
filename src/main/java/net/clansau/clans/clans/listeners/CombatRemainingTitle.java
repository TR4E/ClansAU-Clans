package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.general.combat.Combat;
import net.clansau.core.general.combat.events.CustomCombatEvent;
import net.clansau.core.utility.TitleManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CombatRemainingTitle extends CoreListener<ClanManager> {

    public CombatRemainingTitle(final ClanManager manager) {
        super(manager, "Combat Remaining Title");
    }

    @EventHandler
    public void onCombatRemainingTitle(final CustomCombatEvent e) {
        if (!(e.getCode().equals("Combat Remaining Title"))) {
            return;
        }
        final Combat combat = e.getCombat();
        if (combat == null) {
            return;
        }
        final Player player = e.getPlayer();
        if (!(getManager().isSafe(player.getLocation()))) {
            return;
        }
        getInstance().getManager(TitleManager.class).sendPlayerNoFlicker(player, " ", ChatColor.RED + "Unsafe for " + ChatColor.GREEN + combat.getRemainingString(), 1);
    }
}