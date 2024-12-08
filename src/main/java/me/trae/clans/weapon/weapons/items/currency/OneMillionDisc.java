package me.trae.clans.weapon.weapons.items.currency;

import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.types.CurrencyItem;
import me.trae.clans.weapon.types.enums.CurrencyItemType;

public class OneMillionDisc extends CurrencyItem {

    public OneMillionDisc(final WeaponManager manager) {
        super(manager);
    }

    @Override
    public int getModel() {
        return 810466;
    }

    @Override
    public CurrencyItemType getCurrencyItemType() {
        return CurrencyItemType.ONE_MILLION;
    }
}