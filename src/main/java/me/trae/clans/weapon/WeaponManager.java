package me.trae.clans.weapon;

import me.trae.clans.Clans;
import me.trae.clans.weapon.weapons.items.FiftyGrandDisc;
import me.trae.clans.weapon.weapons.items.FiveHundredGrandDisc;
import me.trae.clans.weapon.weapons.items.HundredGrandDisc;
import me.trae.clans.weapon.weapons.items.OneMillionDisc;
import me.trae.clans.weapon.weapons.legendaries.WindBlade;
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

        // Legendaries
        addModule(new WindBlade(this));
    }
}