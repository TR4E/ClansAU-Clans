package me.trae.clans.weapon.weapons.items;

import me.trae.api.damage.events.CustomDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.CustomItem;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class FireAxe extends CustomItem<Clans, WeaponManager, WeaponData> implements Listener {

    @ConfigInject(type = Long.class, name = "Duration", defaultValue = "4000")
    private long duration;

    public FireAxe(final WeaponManager manager) {
        super(manager, new ItemStack(Material.GOLD_AXE));
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Ignite enemies on fire."
        };
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomDamage(final CustomDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }

        if (!(event.getDamagee() instanceof LivingEntity)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        final LivingEntity damagee = event.getDamageeByClass(LivingEntity.class);
        final Player damager = event.getDamagerByClass(Player.class);

        if (!(this.hasWeaponByPlayer(damager))) {
            return;
        }

        damagee.setFireTicks((int) this.duration / 50);
    }
}