package me.trae.clans.weapon.weapons.legendaries;

import me.trae.api.damage.events.damage.CustomDamageEvent;
import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.utility.UtilString;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class GiantsBroadsword extends Legendary<Clans, WeaponManager, WeaponData> implements Listener {

    @ConfigInject(type = Double.class, path = "Damage", defaultValue = "10.0")
    private double damage;

    @ConfigInject(type = Long.class, path = "Delay", defaultValue = "1000")
    private long delay;

    public GiantsBroadsword(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "You deal massive damage, however you",
                "attack slower than usual.",
                "",
                UtilString.pair("<gray>Damage", String.format("<yellow>%s", this.damage)),
                UtilString.pair("<gray>Ability", "<yellow>Bonus Damage"),
                UtilString.pair("<gray>Passive", "<yellow>Slow Attack Speed")
        };
    }

    @EventHandler
    public void onCustomDamage(final CustomDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (!(event.getDamagee() instanceof LivingEntity)) {
            return;
        }

        final Player player = event.getDamagerByClass(Player.class);

        if (!(this.hasWeaponByPlayer(player))) {
            return;
        }

        event.setDamage(this.damage);
    }

    @EventHandler
    public void onCustomPreDamage(final CustomPreDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (!(event.getDamagee() instanceof LivingEntity)) {
            return;
        }

        final Player player = event.getDamagerByClass(Player.class);

        if (!(this.hasWeaponByPlayer(player))) {
            return;
        }

        event.setDelay(this.delay);
    }
}