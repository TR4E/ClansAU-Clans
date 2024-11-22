package me.trae.clans.koth.interfaces;

import me.trae.clans.weapon.weapons.items.components.SupplyCrateComponent;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public interface IKoth extends SupplyCrateComponent {

    boolean isActive();

    boolean isChestLocked();

    Map<UUID, Long> getChestOpenerMap();

    void addChestOpener(final Player player);

    void removeChestOpener(final Player player);

    boolean canOpenChest(final Player player);

    boolean isChestOpener(final Player player);

    void start();

    void stop();
}