package me.trae.clans.weapon.weapons;

import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilMessage;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.ChannelLegendary;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class WindBlade extends ChannelLegendary<Clans, WeaponManager, WeaponData> {

    public WindBlade(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
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
    public boolean canActivate(final Player player) {
        if (UtilBlock.isInLiquid(player.getLocation())) {
            UtilMessage.simpleMessage(player, "Weapon", "You cannot use <green><var></green> while in liquid.", Collections.singletonList(this.getAbilityName()));
            return false;
        }

        return true;
    }

    @Override
    public void onUsing(final Player player, final WeaponData data) {
        UtilMessage.message(player, "Wind Blade", "Using...");
    }
}