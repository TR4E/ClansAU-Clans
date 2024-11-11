package me.trae.clans.weapon.weapons.items;

import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.types.CurrencyItem;
import me.trae.clans.weapon.types.enums.CurrencyItemType;

public class OneMillionDisc extends CurrencyItem {

    public OneMillionDisc(final WeaponManager manager) {
        super(manager);
    }

    @Override
    public CurrencyItemType getCurrencyItemType() {
        return CurrencyItemType._1MILL;
    }
}