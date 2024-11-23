package me.trae.clans.clan.modules.skill;

import me.trae.api.champions.skill.SkillLocationEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleSkillLocation extends SpigotListener<Clans, ClanManager> {

    public HandleSkillLocation(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSkillLocation(final SkillLocationEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (this.getManager().canCast(event.getLocation())) {
            return;
        }

        event.setCancelled(true);
    }
}