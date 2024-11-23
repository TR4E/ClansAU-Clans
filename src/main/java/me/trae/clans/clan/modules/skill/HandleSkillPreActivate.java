package me.trae.clans.clan.modules.skill;

import me.trae.api.champions.skill.events.SkillPreActivateEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleSkillPreActivate extends SpigotListener<Clans, ClanManager> {

    public HandleSkillPreActivate(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSkillPreActivate(final SkillPreActivateEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        if (this.getManager().canCast(player)) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.message(player, "Clans", "You are not allowed to cast abilities here!");
    }
}