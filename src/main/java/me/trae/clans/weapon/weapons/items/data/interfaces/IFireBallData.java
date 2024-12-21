package me.trae.clans.weapon.weapons.items.data.interfaces;

import org.bukkit.entity.LargeFireball;

import java.util.List;

public interface IFireBallData {

    List<LargeFireball> getFireballs();

    void addFireball(final LargeFireball fireball);
}