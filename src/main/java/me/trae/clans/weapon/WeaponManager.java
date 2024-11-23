package me.trae.clans.weapon;

import me.trae.clans.Clans;
import me.trae.clans.weapon.weapons.items.*;
import me.trae.clans.weapon.weapons.items.currency.FiftyGrandDisc;
import me.trae.clans.weapon.weapons.items.currency.FiveHundredGrandDisc;
import me.trae.clans.weapon.weapons.items.currency.HundredGrandDisc;
import me.trae.clans.weapon.weapons.items.currency.OneMillionDisc;
import me.trae.clans.weapon.weapons.legendaries.*;
import me.trae.core.weapon.abstracts.AbstractWeaponManager;

public class WeaponManager extends AbstractWeaponManager<Clans> {

    public WeaponManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Items
        addModule(new FiftyGrandDisc(this));
        addModule(new FiveHundredGrandDisc(this));
        addModule(new HundredGrandDisc(this));
        addModule(new OneMillionDisc(this));

        addModule(new EnergyApple(this));
        addModule(new EtherealPearl(this));
        addModule(new FireAxe(this));
        addModule(new HealingPotion(this));
        addModule(new SupplyCrate(this));

        // Legendaries
        addModule(new AlligatorsTooth(this));
        addModule(new DwarvenPickaxe(this));
        addModule(new GiantsBroadsword(this));
        addModule(new HyperAxe(this));
        addModule(new LightningScythe(this));
        addModule(new WindBlade(this));
    }
}