package me.trae.clans.fields.modules.damage;

import me.trae.api.damage.events.armour.ArmourDurabilityEvent;
import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class DisableArmourDurabilityInFields extends SpigotListener<Clans, FieldsManager> {

    public DisableArmourDurabilityInFields(final FieldsManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onArmourDurability(final ArmourDurabilityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        final Player player = event.getEntityByClass(Player.class);

        if (!(this.getManager().isInFields(player.getLocation()))) {
            return;
        }

        event.setCancelled(true);
    }
}