package net.clansau.clans.fields;

import org.bukkit.Location;
import org.bukkit.Material;

public class Fields {

    private final Location location;
    private final Material material;
    private final byte data;

    public Fields(final Location location, final Material material, final int data) {
        this.location = location;
        this.material = material;
        this.data = (byte) data;
    }

    public final Location getLocation() {
        return this.location;
    }

    public final Material getMaterial() {
        return this.material;
    }

    public final byte getData() {
        return this.data;
    }
}