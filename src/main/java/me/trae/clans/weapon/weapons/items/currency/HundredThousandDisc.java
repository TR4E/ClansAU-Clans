package me.trae.clans.weapon.weapons.items.currency;

import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.types.CurrencyItem;
import me.trae.clans.weapon.types.enums.CurrencyItemType;

public class HundredThousandDisc extends CurrencyItem {

    public HundredThousandDisc(final WeaponManager manager) {
        super(manager);
    }

    @Override
    public CurrencyItemType getCurrencyItemType() {
        return CurrencyItemType.HUNDRED_THOUSAND;
    }
}