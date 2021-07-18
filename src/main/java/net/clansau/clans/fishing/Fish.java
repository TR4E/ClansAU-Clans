package net.clansau.clans.fishing;

import java.util.UUID;

public class Fish {

    private final UUID uuid;
    private final String name;
    private final int size;
    private final long sysTime;

    public Fish(final UUID uuid, final String name, final int size, final long sysTime) {
        this.uuid = uuid;
        this.name = name;
        this.size = size;
        this.sysTime = sysTime;
    }

    public final UUID getUUID() {
        return this.uuid;
    }

    public final String getName() {
        return this.name;
    }

    public final int getSize() {
        return this.size;
    }

    public final long getSysTime() {
        return this.sysTime;
    }
}