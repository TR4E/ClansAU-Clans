package me.trae.clans.weapon.weapons.items;

import me.trae.api.combat.CombatManager;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.effect.Effect;
import me.trae.core.effect.EffectManager;
import me.trae.core.effect.constants.PotionEffectConstants;
import me.trae.core.effect.data.EffectData;
import me.trae.core.effect.models.NegativeEffect;
import me.trae.core.effect.registry.EffectRegistry;
import me.trae.core.effect.types.NoFall;
import me.trae.core.throwable.Throwable;
import me.trae.core.throwable.ThrowableManager;
import me.trae.core.throwable.events.ThrowableGroundedEvent;
import me.trae.core.throwable.events.ThrowableUpdaterEvent;
import me.trae.core.utility.*;
import me.trae.core.utility.enums.ActionType;
import me.trae.core.utility.objects.SoundCreator;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.ActiveCustomItem;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Random;

public class EtherealPearl extends ActiveCustomItem<Clans, WeaponManager, WeaponData> implements Listener {

    @ConfigInject(type = Long.class, path = "Left-Click-Recharge", defaultValue = "15_000")
    private long leftClickRecharge;

    @ConfigInject(type = Long.class, path = "Right-Click-Recharge", defaultValue = "5_000")
    private long rightClickRecharge;

    @ConfigInject(type = Double.class, path = "Item-Velocity", defaultValue = "1.8")
    private double itemVelocity;

    @ConfigInject(type = Integer.class, path = "Particle-Count", defaultValue = "32")
    private int particleCount;

    public EtherealPearl(final WeaponManager manager) {
        super(manager, new ItemStack(Material.ENDER_PEARL), ActionType.ALL);
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public boolean isNaturallyObtained() {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                UtilString.pair("<gray>Left-Click", "<yellow>Throw"),
                "   Ride Pearl",
                "",
                UtilString.pair("<gray>Right-Click", "<yellow>Consume"),
                "   Removes Negative Effects"
        };
    }

    @Override
    public String getAbilityName(final ActionType actionType) {
        switch (actionType) {
            case LEFT_CLICK:
                return "Ride Pearl";
            case RIGHT_CLICK:
                return "Consume Pearl";
        }

        return this.getAbilityName();
    }

    @Override
    public void onActivate(final Player player, final ActionType actionType) {
        switch (actionType) {
            case LEFT_CLICK: {
                this.onLeftClick(player);
                break;
            }
            case RIGHT_CLICK: {
                this.onRightClick(player);
                break;
            }
        }
    }

    @Override
    public boolean canActivate(final Player player, final ActionType actionType) {
        if (!(super.canActivate(player, actionType))) {
            return false;
        }

        if (actionType == ActionType.LEFT_CLICK) {
            if (this.getInstance(Core.class).getManagerByClass(CombatManager.class).isCombatByPlayer(player)) {
                UtilMessage.simpleMessage(player, "Item", "You cannot use <green><var></green> while in combat.", Collections.singletonList(this.getAbilityName(actionType)));
                return false;
            }
        }

        return true;
    }

    private void onLeftClick(final Player player) {
        final Throwable throwable = new Throwable(this.getAbilityName(ActionType.LEFT_CLICK), this.getItemStack(), player, -1L, player.getLocation().getDirection().multiply(this.itemVelocity));

        this.getInstance(Core.class).getManagerByClass(ThrowableManager.class).addThrowable(throwable);

        throwable.getItem().setPassenger(player);

        this.getInstance(Core.class).getManagerByClass(EffectManager.class).getModuleByClass(NoFall.class).addUser(new EffectData(player) {
            @Override
            public boolean isRemoveOnAction() {
                return true;
            }
        });

        UtilMessage.simpleMessage(player, this.getName(), "You used <var>.", Collections.singletonList(this.getDisplayAbilityName(ActionType.LEFT_CLICK)));
    }

    private void onRightClick(final Player player) {
        for (final Effect<?, ?> effect : EffectRegistry.getEffects()) {
            if (!(effect.isUserByEntity(player))) {
                continue;
            }

            if (!(effect instanceof NegativeEffect)) {
                continue;
            }

            effect.removeUser(effect.getUserByEntity(player));
        }

        for (final PotionEffectType potionEffectType : PotionEffectConstants.NEGATIVE_POTION_EFFECT_TYPES) {
            UtilEntity.removePotionEffect(player, potionEffectType);
        }

        new SoundCreator(Sound.EAT).play(player.getLocation());

        UtilMessage.message(player, this.getName(), "You removed all negative effects!");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onThrowableGrounded(final ThrowableGroundedEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.isThrowableName(this.getAbilityName(ActionType.LEFT_CLICK)))) {
            return;
        }

        event.setCancelled(true);

        final Item item = event.getThrowable().getItem();
        if (item == null) {
            return;
        }

        final Entity passenger = item.getPassenger();
        if (passenger == null) {
            return;
        }

        final Location location = event.getLocation();

        final Random random = new Random();

        for (int i = 0; i < this.particleCount; i++) {
            UtilParticle.playParticle(
                    EnumParticle.PORTAL,
                    location,
                    (float) location.getX() + 0.5F,
                    (float) (location.getY() - 1.0F + random.nextDouble() * 2.0F),
                    (float) location.getZ(), (float) random.nextGaussian(),
                    0.0F, (float) random.nextGaussian(),
                    0.1F,
                    2
            );
        }

        new SoundCreator(Sound.ENDERMAN_TELEPORT).play(location);

        passenger.teleport(passenger.getLocation().add(0.0D, 0.5D, 0.0D));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onThrowableUpdater(final ThrowableUpdaterEvent event) {
        if (!(event.isThrowableName(this.getAbilityName(ActionType.LEFT_CLICK)))) {
            return;
        }

        final Throwable throwable = event.getThrowable();

        final Item item = throwable.getItem();
        if (item == null || item.isDead() || !(item.isValid())) {
            return;
        }

        final Player throwerPlayer = event.getThrowable().getThrowerPlayer();

        if (item.getPassenger() != null && !(item.getPassenger().equals(throwerPlayer))) {
            return;
        }

        if (!(throwerPlayer.isInsideVehicle()) || (throwerPlayer.getVehicle() != null && !(throwerPlayer.getVehicle().equals(item)))) {
            return;
        }

        final Block blockTarget = UtilBlock.getBlockTarget(throwerPlayer, 2);
        if (blockTarget == null) {
            return;
        }

        if (!(blockTarget.getType().isSolid() && UtilBlock.isDoor(blockTarget.getType()))) {
            return;
        }

        throwerPlayer.leaveVehicle();

        item.remove();
    }

    @Override
    public float getEnergy(final ActionType actionType) {
        return 0.0F;
    }

    @Override
    public long getRecharge(final ActionType actionType) {
        switch (actionType) {
            case LEFT_CLICK:
                return this.leftClickRecharge;
            case RIGHT_CLICK:
                return this.rightClickRecharge;
        }

        return 0L;
    }
}