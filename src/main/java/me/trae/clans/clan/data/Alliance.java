package me.trae.clans.clan.data;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.interfaces.IAlliance;

public class Alliance implements IAlliance {

    private final String name;

    private boolean trusted;

    public Alliance(final String name, final boolean trusted) {
        this.name = name;
        this.trusted = trusted;
    }

    public Alliance(final Clan clan) {
        this(clan.getName(), false);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isTrusted() {
        return this.trusted;
    }

    @Override
    public void setTrusted(final boolean trusted) {
        this.trusted = trusted;
    }
}