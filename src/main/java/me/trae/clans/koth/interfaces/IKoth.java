package me.trae.clans.koth.interfaces;

import me.trae.clans.weapon.weapons.items.components.SupplyCrateComponent;

public interface IKoth extends SupplyCrateComponent {

    boolean isActive();

    void start();

    void stop();
}