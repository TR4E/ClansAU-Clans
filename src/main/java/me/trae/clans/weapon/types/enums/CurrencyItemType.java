package me.trae.clans.weapon.types.enums;

import me.trae.clans.weapon.types.enums.interfaces.ICurrencyItemType;
import org.bukkit.Material;

public enum CurrencyItemType implements ICurrencyItemType {

    FIFTY_GRAND("$50k", 50_000, Material.GOLD_RECORD),
    ONE_HUNDRED_GRAND("$100k", 100_000, Material.GREEN_RECORD),
    FIVE_HUNDRED_GRAND("$500k", 500_000, Material.RECORD_4),
    ONE_MILLION("$1mill", 1_000_000, Material.RECORD_11);

    private final String displayName;
    private final int price;
    private final Material material;

    CurrencyItemType(final String displayName, final int price, final Material material) {
        this.displayName = displayName;
        this.price = price;
        this.material = material;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }
}