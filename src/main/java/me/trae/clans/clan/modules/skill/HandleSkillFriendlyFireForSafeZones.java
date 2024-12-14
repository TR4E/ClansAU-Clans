package me.trae.clans.clan.modules.skill;

import me.trae.api.champions.skill.events.SkillFriendlyFireEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleSkillFriendlyFireForSafeZones extends SpigotListener<Clans, ClanManager> {

    public HandleSkillFriendlyFireForSafeZones(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSkillFriendlyFire(final SkillFriendlyFireEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(this.getManager().isSafeByLocation(event.getTarget().getLocation()))) {
            return;
        }

        event.setCancelled(true);
    }
}