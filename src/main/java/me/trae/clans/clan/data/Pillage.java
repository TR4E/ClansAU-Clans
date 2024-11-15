package me.trae.clans.clan.data;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.interfaces.IPillage;

import java.util.Arrays;

public class Pillage implements IPillage {

    private final String name;
    private final long systemTime;

    public Pillage(final String name, final long systemTime) {
        this.name = name;
        this.systemTime = systemTime;
    }

    public Pillage(final Clan clan) {
        this(clan.getName(), System.currentTimeMillis());
    }

    public Pillage(final String[] tokens) {
        this(tokens[0], Long.parseLong(tokens[1]));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getSystemTime() {
        return this.systemTime;
    }

    @Override
    public String toString() {
        return String.join(":", Arrays.asList(this.getName(), String.valueOf(this.getSystemTime())));
    }
}