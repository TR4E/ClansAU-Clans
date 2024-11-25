package me.trae.clans.perk.perks.interfaces;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public interface IAgilityHelmet {

    Map<UUID, Boolean> getActive();

    Material getMaterial();

    boolean isEquipped(final Player player);

    boolean isUsing(final Player player);

    void unEquip(final Player player);
}