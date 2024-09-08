package me.trae.clans.weapon;

import me.trae.clans.Clans;
import me.trae.clans.weapon.weapons.WindBlade;
import me.trae.core.weapon.abstracts.AbstractWeaponManager;

public class WeaponManager extends AbstractWeaponManager<Clans> {

    public WeaponManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new WindBlade(this));
    }
}