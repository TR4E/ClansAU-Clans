package me.trae.clans.weapon.types;

import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.weapon.types.interfaces.ICurrencyItem;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.CustomItem;
import org.bukkit.inventory.ItemStack;

public abstract class CurrencyItem extends CustomItem<Clans, WeaponManager, WeaponData> implements ICurrencyItem {

    public CurrencyItem(final WeaponManager manager) {
        super(manager, null);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(this.getCurrencyItemType().getMaterial());
    }

    @Override
    public String getDisplayName() {
        return String.format("<green>%s", this.getCurrencyItemType().getDisplayName());
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                String.format("<white>This is worth <gold>$%s", this.getCurrencyItemType().getPrice())
        };
    }

    @Override
    public boolean isNaturallyObtained() {
        return true;
    }

    @Override
    public boolean showInMenu() {
        return false;
    }
}