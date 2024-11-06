package me.trae.clans.weapon.types.enums;

import me.trae.clans.weapon.types.enums.interfaces.ICurrencyItemType;
import org.bukkit.Material;

public enum CurrencyItemType implements ICurrencyItemType {

    _50K("$50,000", Material.GOLD_RECORD),
    _100K("$100,000", Material.GREEN_RECORD),
    _500K("$500,000", Material.RECORD_4),
    _1MILL("$1,000,000", Material.RECORD_11);

    private final String displayName;
    private final Material material;

    CurrencyItemType(final String displayName, final Material material) {
        this.displayName = displayName;
        this.material = material;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public int getPrice() {
        String displayName = this.getDisplayName();

        displayName = displayName.replaceAll("\\$", "");
        displayName = displayName.replaceAll(",", "");

        return Integer.parseInt(displayName);
    }
}