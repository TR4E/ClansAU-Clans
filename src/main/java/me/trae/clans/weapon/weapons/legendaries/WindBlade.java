package me.trae.clans.weapon.weapons.legendaries;

import me.trae.api.damage.events.CustomDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.UtilVelocity;
import me.trae.core.utility.objects.SoundCreator;
import me.trae.core.weapon.data.types.ChannelWeaponData;
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

public class WindBlade extends ChannelLegendary<Clans, WeaponManager, ChannelWeaponData> implements Listener {

    @ConfigInject(type = Double.class, path = "Damage", defaultValue = "7.0")
    private double damage;

    @ConfigInject(type = Double.class, path = "Knockback", defaultValue = "3.0")
    private double knockback;

    @ConfigInject(type = Double.class, path = "Strength", defaultValue = "0.9")
    private double strength;

    @ConfigInject(type = Double.class, path = "yAdd", defaultValue = "0.11")
    private double yAdd;

    @ConfigInject(type = Double.class, path = "yMax", defaultValue = "1.0")
    private double yMax;

    @ConfigInject(type = Boolean.class, path = "groundBoost", defaultValue = "true")
    private boolean groundBoost;


    public WindBlade(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public Class<ChannelWeaponData> getClassOfData() {
        return ChannelWeaponData.class;
    }

    @Override
    public String[] getDescription() {
        final String damage = String.valueOf(this.damage);
        final String knockback = this.knockback * 100 + "%";

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
        this.addUser(new ChannelWeaponData(player));
    }

    @Override
    public void onUsing(final Player player, final ChannelWeaponData data) {
        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.SNOW_BLOCK);

        new SoundCreator(Sound.FIZZ, 0.5F, 1.5F).play(player.getLocation());

        UtilJava.call(this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player), client -> {
            double strength = this.strength;

            if (client.isAdministrating()) {
                strength += 1.0D;
            }

            UtilVelocity.velocity(player, strength, this.yAdd, this.yMax, this.groundBoost);
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

        event.setDamage(this.damage);
        event.setKnockback(this.knockback);
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

    @Override
    public float getEnergy() {
        return 0.0F;
    }

    @Override
    public float getEnergyNeeded() {
        return 30.0F;
    }

    @Override
    public float getEnergyUsing() {
        return 2.0F;
    }

    @Override
    public long getRecharge() {
        return 0;
    }
}