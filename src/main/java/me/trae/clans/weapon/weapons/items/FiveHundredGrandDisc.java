package me.trae.clans.weapon.weapons.items;

import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.types.CurrencyItem;
import me.trae.clans.weapon.types.enums.CurrencyItemType;

public class FiveHundredGrandDisc extends CurrencyItem {

    public FiveHundredGrandDisc(final WeaponManager manager) {
        super(manager);
    }

    @Override
    public CurrencyItemType getCurrencyItemType() {
        return CurrencyItemType._500K;
    }
}