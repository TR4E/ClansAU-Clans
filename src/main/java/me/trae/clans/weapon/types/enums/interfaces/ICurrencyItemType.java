package me.trae.clans.weapon.types.enums.interfaces;

import org.bukkit.Material;

public interface ICurrencyItemType {

    String getDisplayName();

    Material getMaterial();

    int getPrice();
}