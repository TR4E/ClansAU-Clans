package me.trae.clans.weapon.weapons.legendaries;

import me.trae.api.damage.events.damage.CustomDamageEvent;
import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.UtilVelocity;
import me.trae.core.utility.injectors.annotations.Inject;
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

    @Inject
    private ClientManager clientManager;

    @ConfigInject(type = Float.class, path = "Energy-Needed", defaultValue = "20.0")
    private float energyNeeded;

    @ConfigInject(type = Float.class, path = "Energy-Using", defaultValue = "2.4")
    private float energyUsing;

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
    public int getModel() {
        return 674192;
    }

    @Override
    public Class<ChannelWeaponData> getClassOfData() {
        return ChannelWeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Once owned by the God Zephyrus,",
                "it is rumoured the Wind Blade",
                "grants it's owner flight.",
                "",
                UtilString.pair("<gray>Damage", UtilString.format("<yellow>%s", this.damage)),
                UtilString.pair("<gray>Ability", UtilString.format("<yellow>%s", this.getAbilityName())),
                UtilString.pair("<gray>Knockback", UtilString.format("<yellow>%s", (this.knockback * 100) + "%"))
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

        UtilJava.call(this.clientManager.getClientByPlayer(player), client -> {
            double strength = this.strength;

            if (client.isAdministrating()) {
                strength += 1.0D;
            }

            UtilVelocity.velocity(player, strength, this.yAdd, this.yMax, this.groundBoost);
        });
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

        if (!(this.hasWeaponByPlayer(event.getDamagerByClass(Player.class)))) {
            return;
        }

        event.setDamage(this.damage);
        event.setKnockback(this.knockback * 10);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPreDamage(final CustomPreDamageEvent event) {
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
        return this.energyNeeded;
    }

    @Override
    public float getEnergyUsing() {
        return this.energyUsing;
    }

    @Override
    public long getRecharge() {
        return 0L;
    }
}