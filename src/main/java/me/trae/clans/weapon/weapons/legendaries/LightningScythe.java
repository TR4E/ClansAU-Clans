package me.trae.clans.weapon.weapons.legendaries;

import me.trae.api.damage.utility.UtilDamage;
import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.utility.*;
import me.trae.core.utility.enums.ActionType;
import me.trae.core.utility.objects.SoundCreator;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.events.WeaponFriendlyFireEvent;
import me.trae.core.weapon.events.WeaponLocationEvent;
import me.trae.core.weapon.types.ActiveLegendary;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class LightningScythe extends ActiveLegendary<Clans, WeaponManager, WeaponData> {

    @ConfigInject(type = Float.class, path = "Energy", defaultValue = "40.0")
    private float energy;

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "10_000")
    private long recharge;

    @ConfigInject(type = Double.class, path = "Damage", defaultValue = "15.0")
    private double damage;

    @ConfigInject(type = Double.class, path = "Entity-Damage-Distance", defaultValue = "3.0")
    private double entityDamageDistance;

    @ConfigInject(type = Double.class, path = "Entity-Sound-Distance", defaultValue = "100.0")
    private double entitySoundDistance;

    @ConfigInject(type = Integer.class, path = "Block-Distance", defaultValue = "50")
    private int blockDistance;

    @ConfigInject(type = Integer.class, path = "Strike-Amount", defaultValue = "2")
    private int strikeAmount;

    @ConfigInject(type = Integer.class, path = "Take-Durability", defaultValue = "5")
    private int takeDurability;

    @ConfigInject(type = Boolean.class, path = "Friendly-Fire", defaultValue = "false")
    private boolean friendlyFire;

    public LightningScythe(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_HOE), ActionType.RIGHT_CLICK);
    }

    @Override
    public int getModel() {
        return 446757;
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public boolean cancelOriginalPlayerInteractEvent() {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "This mysterious weapon is believed",
                "to call the power of the gods",
                "and strike lightning on your enemies.",
                "",
                UtilString.pair("<gray>Damage", UtilString.format("<yellow>%s (AoE)", this.damage)),
                UtilString.pair("<gray>Ability", UtilString.format("<yellow>%s", this.getAbilityName()))
        };
    }

    @Override
    public String getAbilityName() {
        return "Lightning Strike";
    }

    @Override
    public void onActivate(final Player player) {
        final Block block = UtilBlock.getBlockTarget(player, this.blockDistance);
        if (block == null) {
            return;
        }

        this.handleEffect(block);
        this.handleSound(player, block);
        this.handleDamage(player, block);

        UtilItem.takeDurability(player, player.getEquipment().getItemInHand(), this.takeDurability, false, true);

        UtilMessage.simpleMessage(player, this.getName(), "You used <green><var></green>.", Collections.singletonList(this.getAbilityName()));
    }

    private void handleEffect(final Block block) {
        for (int i = 0; i < this.strikeAmount; i++) {
            block.getWorld().strikeLightningEffect(block.getLocation());
        }
    }

    private void handleSound(final Player player, final Block block) {
        new SoundCreator(Sound.AMBIENCE_THUNDER).play(player);

        for (final Player targetPlayer : UtilEntity.getNearbyEntities(Player.class, block.getLocation(), this.entitySoundDistance)) {
            if (targetPlayer == player) {
                continue;
            }

            new SoundCreator(Sound.AMBIENCE_THUNDER).play(targetPlayer);
        }
    }

    private void handleDamage(final Player player, final Block block) {
        for (final LivingEntity targetEntity : UtilEntity.getNearbyEntities(LivingEntity.class, block.getLocation(), this.entityDamageDistance)) {
            if (UtilServer.getEvent(new WeaponLocationEvent(this, targetEntity.getLocation())).isCancelled()) {
                continue;
            }

            if (targetEntity instanceof Player) {
                final WeaponFriendlyFireEvent friendlyFireEvent = new WeaponFriendlyFireEvent(this, player, UtilJava.cast(Player.class, targetEntity));
                UtilServer.callEvent(friendlyFireEvent);
                if (friendlyFireEvent.isCancelled()) {
                    continue;
                }

                if (!(this.friendlyFire)) {
                    if (targetEntity == player) {
                        continue;
                    }

                    if (!(friendlyFireEvent.isVulnerable())) {
                        continue;
                    }
                }
            }

            UtilDamage.damage(targetEntity, player, EntityDamageEvent.DamageCause.CUSTOM, this.damage, this.getDisplayName(), 1L);
        }
    }

    @Override
    public float getEnergy() {
        return this.energy;
    }

    @Override
    public long getRecharge() {
        return this.recharge;
    }
}