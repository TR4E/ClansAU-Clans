package me.trae.clans.weapon.weapons.items.currency;

import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.types.CurrencyItem;
import me.trae.clans.weapon.types.enums.CurrencyItemType;

public class FiftyThousandDisc extends CurrencyItem {

    public FiftyThousandDisc(final WeaponManager manager) {
        super(manager);
    }

    @Override
    public CurrencyItemType getCurrencyItemType() {
        return CurrencyItemType.FIFTY_THOUSAND;
    }
}