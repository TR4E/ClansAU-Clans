package me.trae.clans.weapon.weapons.legendaries;

import me.trae.api.damage.events.CustomDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.UtilVelocity;
import me.trae.core.utility.objects.SoundCreator;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.ChannelLegendary;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class WindBlade extends ChannelLegendary<Clans, WeaponManager, WeaponData> implements Listener {

    public WindBlade(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));

        this.addPrimitive("Damage", 7.0D);
        this.addPrimitive("Knockback-Amount", 3.0D);

        this.addPrimitive("Strength", 0.9D);
        this.addPrimitive("yAdd", 0.11D);
        this.addPrimitive("yMax", 1.0D);
        this.addPrimitive("groundBoost", true);
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        final String damage = this.getPrimitiveCasted(Double.class, "Damage").toString();
        final String knockback = this.getPrimitiveCasted(Double.class, "Knockback-Amount").intValue() * 100 + "%";

        return new String[]{
                "Once owned by the God Zephyrus,",
                "it is rumoured the Wind Blade",
                "grants it's owner flight.",
                "",
                UtilString.pair("<gray>Damage", String.format("<yellow>%s", damage)),
                UtilString.pair("<gray>Ability", String.format("<yellow>%s", this.getAbilityName())),
                UtilString.pair("<gray>Knockback", String.format("<yellow>%s", knockback))
        };
    }

    @Override
    public String getAbilityName() {
        return "Wind Rider";
    }

    @Override
    public void onActivate(final Player player) {
        this.addUser(new WeaponData(player));
    }

    @Override
    public void onUsing(final Player player, final WeaponData data) {
        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.SNOW_BLOCK);

        new SoundCreator(Sound.FIZZ, 0.5F, 1.5F).play(player.getLocation());

        UtilJava.call(this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player), client -> {
            double strength = this.getPrimitiveCasted(Double.class, "Strength");
            final double yAdd = this.getPrimitiveCasted(Double.class, "yAdd");
            final double yMax = this.getPrimitiveCasted(Double.class, "yMax");
            final boolean groundBoost = this.getPrimitiveCasted(Boolean.class, "groundBoost");

            if (client.isAdministrating()) {
                strength += 1.0D;
            }

            UtilVelocity.velocity(player, strength, yAdd, yMax, groundBoost);
        });
    }

    @EventHandler
    public void onCustomDamageForEntityAttack(final CustomDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (!(this.hasWeaponByPlayer(event.getDamagerByClass(Player.class)))) {
            return;
        }

        event.setDamage(this.getPrimitiveCasted(Double.class, "Damage"));
        event.setKnockback(this.getPrimitiveCasted(Double.class, "Knockback-Amount"));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCustomDamageForFall(final CustomDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        if (!(event.getDamagee() instanceof Player)) {
            return;
        }

        if (!(this.hasWeaponByPlayer(event.getDamageeByClass(Player.class)))) {
            return;
        }

        event.setCancelled(true);
    }
}