package me.trae.clans.weapon.weapons.legendaries;

import me.trae.api.damage.events.CustomDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.utility.*;
import me.trae.core.utility.objects.SoundCreator;
import me.trae.core.weapon.data.WeaponData;
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

public class AlligatorsTooth extends ChannelLegendary<Clans, WeaponManager, WeaponData> implements Listener {

    // TO-DO: Maybe add a Listener Class to remove "Positive Potion Effects" on Player Join/Quit
    private final List<PotionEffectType> POTION_EFFECT_TYPES = Arrays.asList(PotionEffectType.NIGHT_VISION, PotionEffectType.WATER_BREATHING);

    public AlligatorsTooth(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));

        this.addPrimitive("Damage", 7.0D);
        this.addPrimitive("Damage-In-Water", 2.0D);
        this.addPrimitive("Knockback", 3.0D);

        this.addPrimitive("Strength", 1.3D);
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
        final String damage = String.format("%s (+%s in water)", this.getPrimitiveCasted(Double.class, "Damage"), this.getPrimitiveCasted(Double.class, "Damage-In-Water"));

        return new String[]{
                "A blade forged from hundreds of",
                "alligators teeth. It's powers allow",
                "it's owner to swim with great speed,",
                "able to catch any prey.",
                "",
                UtilString.pair("<gray>Damage", String.format("<yellow>%s", damage)),
                UtilString.pair("<gray>Ability", String.format("<yellow>%s", this.getAbilityName())),
                UtilString.pair("<gray>Passive", "<yellow>Water Breathing")
        };
    }

    @Override
    public String getAbilityName() {
        return "Alligators Rush";
    }

    @Override
    public void onActivate(final Player player) {
        this.addUser(new WeaponData(player));
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
        }
    }

    @Override
    public void onUsing(final Player player, final WeaponData data) {
        for (final PotionEffectType potionEffectType : this.POTION_EFFECT_TYPES) {
            if (!(UtilEntity.hasPotionEffect(player, potionEffectType, 255))) {
                UtilEntity.givePotionEffect(player, potionEffectType, 255, Integer.MAX_VALUE);
            }
        }

        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.WATER, 1);

        new SoundCreator(Sound.SWIM, 0.8F, 1.5F).play(player.getLocation());

        UtilJava.call(this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player), client -> {
            double strength = this.getPrimitiveCasted(Double.class, "Strength");
            final double yAdd = this.getPrimitiveCasted(Double.class, "yAdd");
            final double yMax = this.getPrimitiveCasted(Double.class, "yMax");
            final boolean groundBoost = this.getPrimitiveCasted(Boolean.class, "groundBoost");

            if (client.isAdministrating()) {
                strength += 2.0D;
            }

            UtilVelocity.velocity(player, strength, yAdd, yMax, groundBoost);
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

        double damage = this.getPrimitiveCasted(Double.class, "Damage");

        if (UtilBlock.isInWater(player.getLocation())) {
            damage += this.getPrimitiveCasted(Double.class, "Damage-In-Water");
        }

        event.setDamage(damage);
    }
}