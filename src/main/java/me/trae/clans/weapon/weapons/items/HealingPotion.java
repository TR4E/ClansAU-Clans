package me.trae.clans.weapon.weapons.items;

import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.utility.UtilEntity;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.UtilTime;
import me.trae.core.utility.enums.ActionType;
import me.trae.core.utility.objects.SoundCreator;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.ActiveCustomItem;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;

public class HealingPotion extends ActiveCustomItem<Clans, WeaponManager, WeaponData> {

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "25_000")
    private long recharge;

    @ConfigInject(type = Integer.class, path = "Amplifier", defaultValue = "2")
    private int amplifier;

    @ConfigInject(type = Long.class, path = "Duration", defaultValue = "4000")
    private long duration;

    public HealingPotion(final WeaponManager manager) {
        super(manager, new ItemStack(Material.POTION, 1, (short) 8197), ActionType.RIGHT_CLICK);
    }

    @Override
    public int getModel() {
        return 732910;
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
                String.format("Gain <green>Regeneration %s</green> for <green>%s</green>", this.amplifier, UtilTime.getTime(this.duration)),
                "",
                UtilString.pair("<gray>Right-Click", "<yellow>Drink"),
        };
    }

    @Override
    public void onActivate(final Player player, final ActionType actionType) {
        UtilEntity.givePotionEffect(player, PotionEffectType.REGENERATION, this.amplifier, this.duration);

        player.getWorld().playEffect(player.getEyeLocation(), Effect.HEART, 1);

        new SoundCreator(Sound.DRINK).play(player.getLocation());

        UtilMessage.simpleMessage(player, "Item", "You drank a <var>.", Collections.singletonList(this.getDisplayName()));
    }

    @Override
    public float getEnergy(final ActionType actionType) {
        return 0.0F;
    }

    @Override
    public long getRecharge(final ActionType actionType) {
        return this.recharge;
    }
}