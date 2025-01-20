package me.trae.clans.clan.modules.weapon;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.types.AdminClan;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.weapon.events.WeaponLocationEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleWeaponLocation extends SpigotListener<Clans, ClanManager> {

    public HandleWeaponLocation(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWeaponLocation(final WeaponLocationEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(this.canCancel(event))) {
            return;
        }

        event.setCancelled(true);
    }

    private boolean canCancel(final WeaponLocationEvent event) {
        if (event.getObjectDataSingleton().equals("Block Effect") && event.getWeapon().getSlicedName().equals("ExtinguishingPotion") && event.getLocation().getBlock().getType() == Material.FIRE) {
            final Clan territoryClan = this.getManager().getClanByLocation(event.getLocation());
            if (territoryClan instanceof AdminClan) {
                return true;
            }
        }

        if (event.getPlayer() != null) {
            if (this.getManager().canCast(event.getPlayer())) {
                return false;
            }
        } else {
            if (this.getManager().canCast(event.getLocation())) {
                return false;
            }
        }

        return true;
    }
}