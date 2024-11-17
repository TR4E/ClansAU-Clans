package me.trae.clans.weapon.weapons.items;

import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.Core;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.energy.EnergyManager;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.enums.ActionType;
import me.trae.core.utility.enums.TimeUnit;
import me.trae.core.utility.objects.SoundCreator;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.ActiveCustomItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class EnergyApple extends ActiveCustomItem<Clans, WeaponManager, WeaponData> {

    @ConfigInject(type = Float.class, name = "Energy-Gain", defaultValue = "50.0")
    private float energyGain;

    public EnergyApple(final WeaponManager manager) {
        super(manager, new ItemStack(Material.APPLE), ActionType.RIGHT_CLICK);
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
        final String energyGain = String.valueOf(this.energyGain);

        return new String[]{
                String.format("Instantly restores <green>%s</green> energy", energyGain),
                "",
                UtilString.pair("<gray>Right-Click", "<yellow>Consume")
        };
    }

    @Override
    public void onActivate(final Player player, final ActionType actionType) {
        UtilItem.remove(player, player.getInventory().getItemInHand(), 1);

        this.getInstance(Core.class).getManagerByClass(EnergyManager.class).regenerate(player, this.energyGain);

        new SoundCreator(Sound.EAT).play(player.getLocation());

        UtilMessage.simpleMessage(player, "Item", "You consumed a <var>.", Collections.singletonList(this.getDisplayName()));
    }

    @Override
    public boolean canActivate(final Player player, final ActionType actionType) {
        if (!(super.canActivate(player, actionType))) {
            return false;
        }

        if (player.getExp() >= this.getInstance(Core.class).getManagerByClass(EnergyManager.class).MAX_ENERGY) {
            UtilMessage.message(player, this.getAbilityName(), "You are already full of energy.");
            return false;
        }

        return true;
    }

    @Override
    public float getEnergy(final ActionType actionType) {
        return 0.0F;
    }

    @Override
    public long getRecharge(final ActionType actionType) {
        return TimeUnit.SECONDS.getDuration() * 30;
    }
}