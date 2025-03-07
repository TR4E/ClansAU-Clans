package me.trae.clans.weapon.weapons.legendaries;

import me.trae.api.damage.events.damage.CustomDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.effect.EffectManager;
import me.trae.core.effect.data.EffectData;
import me.trae.core.effect.types.NoFall;
import me.trae.core.utility.*;
import me.trae.core.utility.objects.SoundCreator;
import me.trae.core.weapon.data.types.ChannelWeaponData;
import me.trae.core.weapon.types.ChannelLegendary;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AlligatorsTooth extends ChannelLegendary<Clans, WeaponManager, ChannelWeaponData> implements Listener {

    private final List<PotionEffectType> POTION_EFFECT_TYPES = Arrays.asList(PotionEffectType.NIGHT_VISION, PotionEffectType.WATER_BREATHING);

    @ConfigInject(type = Float.class, path = "Energy-Needed", defaultValue = "30.0")
    private float energyNeeded;

    @ConfigInject(type = Float.class, path = "Energy-Using", defaultValue = "1.4")
    private float energyUsing;

    @ConfigInject(type = Double.class, path = "Damage", defaultValue = "7.0")
    private double damage;

    @ConfigInject(type = Double.class, path = "Damage-Addition-In-Water", defaultValue = "2.0")
    private double damageAdditionInWater;

    @ConfigInject(type = Double.class, path = "Strength", defaultValue = "1.3")
    private double strength;

    @ConfigInject(type = Double.class, path = "yAdd", defaultValue = "0.11")
    private double yAdd;

    @ConfigInject(type = Double.class, path = "yMax", defaultValue = "1.0")
    private double yMax;

    @ConfigInject(type = Boolean.class, path = "groundBoost", defaultValue = "true")
    private boolean groundBoost;

    public AlligatorsTooth(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public int getModel() {
        return 814046;
    }

    @Override
    public Class<ChannelWeaponData> getClassOfData() {
        return ChannelWeaponData.class;
    }

    @Override
    public String[] getDescription() {
        final String damage = UtilString.format("%s (+%s in water)", this.damage, this.damageAdditionInWater);

        return new String[]{
                "A blade forged from hundreds of",
                "alligators teeth. It's powers allow",
                "it's owner to swim with great speed,",
                "able to catch any prey.",
                "",
                UtilString.pair("<gray>Damage", UtilString.format("<yellow>%s", damage)),
                UtilString.pair("<gray>Ability", UtilString.format("<yellow>%s", this.getAbilityName())),
                UtilString.pair("<gray>Passive", "<yellow>Water Breathing")
        };
    }

    @Override
    public String getAbilityName() {
        return "Alligators Rush";
    }

    @Override
    public void onActivate(final Player player) {
        this.addUser(new ChannelWeaponData(player));
    }

    @Override
    public boolean canActivate(final Player player) {
        if (!(UtilBlock.isInWater(player.getLocation()))) {
            UtilMessage.simpleMessage(player, "Weapon", "You cannot use <green><var></green> while out of water.", Collections.singletonList(this.getAbilityName()));
            return false;
        }

        return true;
    }

    @Override
    public void reset(final Player player) {
        if (this.isUserByPlayer(player)) {
            for (final PotionEffectType potionEffectType : this.POTION_EFFECT_TYPES) {
                player.removePotionEffect(potionEffectType);
            }

            this.getInstanceByClass(Core.class).getManagerByClass(EffectManager.class).getModuleByClass(NoFall.class).addUser(new EffectData(player, 3000L) {
                @Override
                public boolean isRemoveOnAction() {
                    return true;
                }
            });
        }
    }

    @Override
    public void onUsing(final Player player, final ChannelWeaponData data) {
        for (final PotionEffectType potionEffectType : this.POTION_EFFECT_TYPES) {
            if (!(UtilEntity.hasPotionEffect(player, potionEffectType, 255))) {
                UtilEntity.givePotionEffect(player, potionEffectType, 255, Integer.MAX_VALUE);
            }
        }

        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.WATER, 1);

        new SoundCreator(Sound.SWIM, 0.8F, 1.5F).play(player.getLocation());

        UtilJava.call(this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player), client -> {
            double strength = this.strength;

            if (client.isAdministrating()) {
                strength += 2.0D;
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

        final Player player = event.getDamagerByClass(Player.class);

        if (!(this.hasWeaponByPlayer(player))) {
            return;
        }

        double damage = this.damage;

        if (UtilBlock.isInWater(player.getLocation())) {
            damage += this.damageAdditionInWater;
        }

        event.setDamage(damage);
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