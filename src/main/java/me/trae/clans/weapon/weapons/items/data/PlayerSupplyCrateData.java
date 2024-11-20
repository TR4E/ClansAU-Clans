package me.trae.clans.weapon.weapons.items.data;

import me.trae.clans.weapon.weapons.items.data.interfaces.IPlayerSupplyCrateData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerSupplyCrateData extends SupplyCrateData implements IPlayerSupplyCrateData {

    private final UUID uuid;

    public PlayerSupplyCrateData(final Location location, final long duration, final Player player) {
        super(location, duration);

        this.uuid = player.getUniqueId();
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }
}