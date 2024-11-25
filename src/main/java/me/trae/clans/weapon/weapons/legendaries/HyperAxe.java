package me.trae.clans.weapon.weapons.legendaries;

import me.trae.api.damage.events.damage.CustomDamageEvent;
import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.energy.EnergyManager;
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

public class HyperAxe extends Legendary<Clans, WeaponManager, WeaponData> implements Listener {

    @ConfigInject(type = Float.class, path = "Energy", defaultValue = "6.0")
    private float energy;

    @ConfigInject(type = Double.class, path = "Damage", defaultValue = "3.0")
    private double damage;

    @ConfigInject(type = Double.class, path = "Knockback", defaultValue = "0.0")
    private double knockback;

    @ConfigInject(type = Long.class, path = "Delay", defaultValue = "100")
    private long delay;

    public HyperAxe(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_AXE));
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Rumoured to attack foes 500% faster",
                "than any other weapon known to man.",
                "",
                UtilString.pair("<gray>Damage", String.format("<yellow>%s", this.damage)),
                UtilString.pair("<gray>Ability", "<yellow>Hyper Speed"),
                UtilString.pair("<gray>Passive", "<yellow>Hyper Attack"),
                UtilString.pair("<gray>Knockback", String.format("<yellow>%s", this.knockback > 0.0D ? (this.knockback * 100) + "%" : "None"))
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

        if (!(this.getInstance(Core.class).getManagerByClass(EnergyManager.class).use(player, this.getName(), this.energy, true))) {
            event.setCancelled(true);
            return;
        }

        event.setDamage(this.damage);
        event.setKnockback(this.knockback);
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

        if (this.getInstance(Core.class).getManagerByClass(EnergyManager.class).isExhausted(player, this.getName(), this.energy, false)) {
            event.setCancelled(true);
            return;
        }

        event.setDelay(this.delay);
    }
}