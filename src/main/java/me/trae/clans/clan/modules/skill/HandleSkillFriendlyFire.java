package me.trae.clans.clan.modules.skill;

import me.trae.api.champions.skill.SkillFriendlyFireEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleSkillFriendlyFire extends SpigotListener<Clans, ClanManager> {

    public HandleSkillFriendlyFire(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSkillFriendlyFire(final SkillFriendlyFireEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (this.getManager().canHurt(event.getPlayer(), event.getTarget())) {
            return;
        }

        event.setVulnerable(false);
    }
}