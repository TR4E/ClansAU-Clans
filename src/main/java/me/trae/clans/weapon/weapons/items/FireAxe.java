package me.trae.clans.weapon.weapons.items;

import me.trae.api.damage.events.damage.CustomDamageEvent;
import me.trae.api.damage.events.damage.CustomPostDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.utility.UtilString;
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

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "4_000")
    private long duration;

    @ConfigInject(type = Double.class, path = "Damage", defaultValue = "5.0")
    private double damage;

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
                "Ignite enemies on fire.",
                "",
                UtilString.pair("<gray>Damage", String.format("<yellow>%s", this.damage)),
                UtilString.pair("<gray>Passive", "<yellow>Fire Attack")
        };
    }

    @EventHandler(priority = EventPriority.LOW)
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

        final Player damager = event.getDamagerByClass(Player.class);

        if (!(this.hasWeaponByPlayer(damager))) {
            return;
        }

        event.setDamage(this.damage);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomPostDamage(final CustomPostDamageEvent event) {
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