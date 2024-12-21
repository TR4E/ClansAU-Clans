package me.trae.clans.weapon.weapons.items;

import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.weapons.items.data.FireBallData;
import me.trae.core.Core;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.effect.EffectManager;
import me.trae.core.effect.data.EffectData;
import me.trae.core.effect.types.NoFall;
import me.trae.core.utility.UtilEntity;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.enums.ActionType;
import me.trae.core.weapon.types.ActiveCustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class FireBall extends ActiveCustomItem<Clans, WeaponManager, FireBallData> implements Listener {

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "10_000")
    private long duration;

    @ConfigInject(type = Long.class, path = "Fire-Duration", defaultValue = "10_000")
    private long fireDuration;

    @ConfigInject(type = Double.class, path = "Damage", defaultValue = "6.0")
    private double damage;

    @ConfigInject(type = Double.class, path = "Distance", defaultValue = "5.0")
    private double distance;

    @ConfigInject(type = Double.class, path = "Fireball-Velocity", defaultValue = "5.0")
    private double fireballVelocity;

    @ConfigInject(type = Float.class, path = "Fireball-Yield", defaultValue = "0.0")
    private float fireballYield;

    @ConfigInject(type = Boolean.class, path = "Fireball-Incendiary", defaultValue = "true")
    private boolean fireballIncendiary;

    public FireBall(final WeaponManager manager) {
        super(manager, new ItemStack(Material.FIREBALL), ActionType.LEFT_CLICK);
    }

    @Override
    public int getModel() {
        return 889489;
    }

    @Override
    public Class<FireBallData> getClassOfData() {
        return FireBallData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Launch a fireball.",
                "",
                "When impacted on entities, they will",
                "launch in mid air.",
                "",
                UtilString.pair("<gray>Left-Click", "<yellow>Launch"),
        };
    }

    @Override
    public void onActivate(final Player player, final ActionType actionType) {
        if (!(this.isUserByPlayer(player))) {
            this.addUser(new FireBallData(player, this.duration));
        }

        final FireBallData data = this.getUserByPlayer(player);

        final LargeFireball fireball = player.launchProjectile(LargeFireball.class);

        fireball.setVelocity(fireball.getVelocity().multiply(this.fireballVelocity));

        fireball.setYield(this.fireballYield);
        fireball.setIsIncendiary(this.fireballIncendiary);

        data.addFireball(fireball);

        this.addUser(data);

        UtilMessage.simpleMessage(player, "Item", "You launched a <var>.", Collections.singletonList(this.getDisplayName()));
    }

    @Override
    public void reset(final Player player) {
        if (this.isUserByPlayer(player)) {
            final FireBallData data = this.getUserByPlayer(player);

            for (final LargeFireball fireball : data.getFireballs()) {
                fireball.remove();
            }
        }

        super.reset(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof LargeFireball)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        final LargeFireball fireball = UtilJava.cast(LargeFireball.class, event.getEntity());

        final FireBallData data = this.getDataByFireball(fireball);
        if (data == null) {
            return;
        }

        final Player player = Bukkit.getPlayer(data.getUUID());

        final NoFall effect = this.getInstance(Core.class).getManagerByClass(EffectManager.class).getModuleByClass(NoFall.class);

        for (final LivingEntity targetEntity : UtilEntity.getNearbyEntities(LivingEntity.class, event.getLocation(), this.distance)) {
            effect.addUser(new EffectData(targetEntity) {
                @Override
                public boolean isRemoveOnAction() {
                    return true;
                }
            });

            targetEntity.setVelocity(targetEntity.getLocation().toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(2.0D).setY(1.0D));
        }

        this.removeUser(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPreDamage(final CustomPreDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }

        if (!(event.getProjectile() instanceof LargeFireball)) {
            return;
        }

        final LargeFireball fireball = event.getProjectileByClass(LargeFireball.class);

        final FireBallData data = this.getDataByFireball(fireball);
        if (data == null) {
            return;
        }

        event.setCancelled(true);
    }

    private FireBallData getDataByFireball(final LargeFireball fireball) {
        for (final FireBallData data : this.getUsers().values()) {
            if (data.getFireballs().isEmpty() || !(data.getFireballs().contains(fireball))) {
                continue;
            }

            return data;
        }

        return null;
    }

    @Override
    public float getEnergy(final ActionType actionType) {
        return 0.0F;
    }

    @Override
    public long getRecharge(final ActionType actionType) {
        return 0L;
    }
}