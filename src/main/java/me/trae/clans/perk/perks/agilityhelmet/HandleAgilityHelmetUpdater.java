package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.champions.utility.UtilRole;
import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HandleAgilityHelmetUpdater extends SpigotUpdater<Clans, PerkManager> {

    private final AgilityHelmet PERK;

    public HandleAgilityHelmetUpdater(final PerkManager manager) {
        super(manager);

        this.PERK = manager.getModuleByClass(AgilityHelmet.class);
    }

    @Update
    public void onUpdater() {
        for (final Player player : UtilServer.getOnlinePlayers()) {
            if (!(this.PERK.isUserByPlayer(player))) {
                this.PERK.unEquip(player);
                continue;
            }

            if (this.PERK.isEquipped(player)) {
                if (!(this.PERK.getActive().containsKey(player.getUniqueId()))) {
                    this.PERK.getActive().put(player.getUniqueId(), false);
                }

                if (!(this.PERK.getActive().get(player.getUniqueId()))) {
                    this.PERK.getActive().put(player.getUniqueId(), true);
                    UtilRole.playEffect(player, "Agility", true);
                }
            } else {
                this.PERK.getActive().remove(player.getUniqueId());
            }
        }

        this.PERK.getActive().keySet().removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
    }
}