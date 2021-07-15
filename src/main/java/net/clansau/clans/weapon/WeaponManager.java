package net.clansau.clans.weapon;

import net.clansau.clans.Clans;
import net.clansau.clans.weapon.listeners.ReceiveLegendaryCooldown;
import net.clansau.clans.weapon.listeners.ReceiveLegendaryOnLastDayOnly;
import net.clansau.core.weapon.framework.IWeaponManager;

public class WeaponManager extends IWeaponManager {

    public WeaponManager(final Clans instance) {
        super(instance);
    }

    @Override
    protected void registerModules() {
        addModule(new ReceiveLegendaryCooldown(this));
        addModule(new ReceiveLegendaryOnLastDayOnly(this));
    }

    @Override
    protected void loadWeapons() {
    }
}