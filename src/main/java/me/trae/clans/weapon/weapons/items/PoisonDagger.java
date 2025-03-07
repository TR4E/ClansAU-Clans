package me.trae.clans.weapon.weapons.items;

import me.trae.api.champions.role.events.RoleCheckEvent;
import me.trae.api.damage.events.damage.CustomDamageEvent;
import me.trae.api.damage.events.damage.CustomPostDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.*;
import me.trae.core.utility.enums.PluginType;
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
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.stream.Stream;

public class PoisonDagger extends CustomItem<Clans, WeaponManager, WeaponData> implements Listener {

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "5_000")
    private long recharge;

    @ConfigInject(type = Double.class, path = "Damage", defaultValue = "4.0")
    private double damage;

    @ConfigInject(type = Long.class, path = "Slowness-Duration", defaultValue = "5_000")
    private long slownessDuration;

    @ConfigInject(type = Integer.class, path = "Slowness-Amplifier", defaultValue = "2")
    private int slownessAmplifier;

    @ConfigInject(type = Long.class, path = "Poison-Duration", defaultValue = "5_000")
    private long poisonDuration;

    @ConfigInject(type = Integer.class, path = "Poison-Amplifier", defaultValue = "1")
    private int poisonAmplifier;

    @ConfigInject(type = Boolean.class, path = "Class-Required", defaultValue = "false")
    private boolean classRequired;

    public PoisonDagger(final WeaponManager manager) {
        super(manager, new ItemStack(Material.SPIDER_EYE));
    }

    @Override
    public int getModel() {
        return 655059;
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Infect enemies with Slowness and Poison.",
                "",
                UtilString.pair("<gray>Damage", UtilString.format("<yellow>%s", this.damage)),
                UtilString.pair("<gray>Ability", "<yellow>Infect")
        };
    }

    private boolean canActivate(final Player player) {
        if (UtilPlugin.isPluginByType(PluginType.CHAMPIONS) && this.classRequired) {
            if (UtilServer.getEvent(new RoleCheckEvent(player)).getRole() == null) {
                return false;
            }
        }

        return true;
    }

    private boolean isAffected(final LivingEntity damagee) {
        if (Stream.of(PotionEffectType.SLOW, PotionEffectType.POISON).noneMatch(potionEffectType -> UtilEntity.hasPotionEffect(damagee, potionEffectType))) {
            return false;
        }

        if (!(UtilEntity.hasPotionEffect(damagee, PotionEffectType.SLOW, this.slownessAmplifier))) {
            return false;
        }

        if (!(UtilEntity.hasPotionEffect(damagee, PotionEffectType.POISON, this.poisonAmplifier))) {
            return false;
        }

        return true;
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

        if (!(this.canActivate(damager))) {
            return;
        }

        if (this.getInstanceByClass(Core.class).getManagerByClass(RechargeManager.class).isCooling(damager, this.getName(), false)) {
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

        if (!(this.canActivate(damager))) {
            return;
        }

        if (!(this.getInstanceByClass(Core.class).getManagerByClass(RechargeManager.class).add(damager, this.getName(), this.recharge, true))) {
            return;
        }

        if (this.isAffected(damagee)) {
            UtilMessage.simpleMessage(damager, this.getName(), "<var> has already been infected.", Collections.singletonList(event.getDamageeName()));
            UtilMessage.simpleMessage(damagee, this.getName(), "<var> has tried to infect you.", Collections.singletonList(event.getDamagerName()));
            return;
        }

        event.setReason(this.getDisplayName(), this.poisonDuration);

        UtilEntity.givePotionEffect(damagee, PotionEffectType.SLOW, this.slownessAmplifier, this.slownessDuration);
        UtilEntity.givePotionEffect(damagee, PotionEffectType.POISON, this.poisonAmplifier, this.poisonDuration);

        if (damagee instanceof Player) {
            UtilMessage.simpleMessage(damager, this.getName(), "You infected <var>.", Collections.singletonList(event.getDamageeName()));
            UtilMessage.simpleMessage(damagee, this.getName(), "<var> has infected you.", Collections.singletonList(event.getDamagerName()));
        } else {
            UtilMessage.simpleMessage(damager, this.getName(), "You infected a <var>.", Collections.singletonList(event.getDamageeName()));
        }
    }
}