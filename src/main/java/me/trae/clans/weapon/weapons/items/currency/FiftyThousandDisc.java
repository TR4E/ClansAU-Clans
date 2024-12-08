package me.trae.clans.weapon.weapons.items.currency;

import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.types.CurrencyItem;
import me.trae.clans.weapon.types.enums.CurrencyItemType;

public class FiftyThousandDisc extends CurrencyItem {

    public FiftyThousandDisc(final WeaponManager manager) {
        super(manager);
    }

    @Override
    public int getModel() {
        return 597037;
    }

    @Override
    public CurrencyItemType getCurrencyItemType() {
        return CurrencyItemType.FIFTY_THOUSAND;
    }
}