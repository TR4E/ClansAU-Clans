package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.general.combat.events.CustomDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class DisableArmorDurabilityInFields extends CoreListener<WorldManager> {

    public DisableArmorDurabilityInFields(final WorldManager manager) {
        super(manager, "Disable Armor Durability In Fields");
    }

    @EventHandler
    public void onCustomDamage(final CustomDamageEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getDamagee() instanceof Player)) {
            return;
        }
        final Player player = e.getDamageePlayer();
        final Clan land = getManager().getClanManager().getClan(player.getLocation());
        if (land == null || !(land instanceof AdminClan && land.getName().equalsIgnoreCase("fields"))) {
            return;
        }
        e.setIgnoreArmour(true);
    }
}