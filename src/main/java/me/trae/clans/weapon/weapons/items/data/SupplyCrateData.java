package me.trae.clans.weapon.weapons.items.data;

import me.trae.clans.weapon.weapons.items.data.interfaces.ISupplyCrateData;
import org.bukkit.Location;

public class SupplyCrateData implements ISupplyCrateData {

    private final Location location;
    private final long systemTime, duration;

    private boolean filled;

    public SupplyCrateData(final Location location, final long duration) {
        this.location = location;
        this.systemTime = System.currentTimeMillis();
        this.duration = duration;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public int getCount() {
        return (int) (this.getRemaining() / 1000L);
    }

    @Override
    public long getSystemTime() {
        return this.systemTime;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public boolean isFilled() {
        return this.filled;
    }

    @Override
    public void setFilled(final boolean filled) {
        this.filled = filled;
    }
}