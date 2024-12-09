package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.api.champions.utility.UtilRole;
import me.trae.clans.Clans;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotSubUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HandleAgilityHelmetUpdater extends SpigotSubUpdater<Clans, AgilityHelmet> {

    public HandleAgilityHelmetUpdater(final AgilityHelmet module) {
        super(module);
    }

    @Update(delay = 125L)
    public void onUpdater() {
        for (final Player player : UtilServer.getOnlinePlayers()) {
            if (!(this.getModule().isUserByPlayer(player))) {
                this.getModule().unEquip(player);
                continue;
            }

            if (this.getModule().isEquipped(player)) {
                if (!(this.getModule().getActive().containsKey(player.getUniqueId()))) {
                    this.getModule().getActive().put(player.getUniqueId(), false);
                }

                if (!(this.getModule().getActive().get(player.getUniqueId()))) {
                    this.getModule().getActive().put(player.getUniqueId(), true);
                    UtilRole.playEffect(player, "Agility", true);
                }
            } else {
                this.getModule().getActive().remove(player.getUniqueId());
            }
        }

        this.getModule().getActive().keySet().removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
    }
}