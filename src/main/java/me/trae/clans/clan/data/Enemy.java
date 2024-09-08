package me.trae.clans.clan.data;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.interfaces.IEnemy;

public class Enemy implements IEnemy {

    private final String name;

    private int dominancePoints;

    public Enemy(final String name, final int dominancePoints) {
        this.name = name;
        this.dominancePoints = dominancePoints;
    }

    public Enemy(final Clan clan) {
        this(clan.getName(), 0);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getDominancePoints() {
        return this.dominancePoints;
    }

    @Override
    public void setDominancePoints(final int dominancePoints) {
        this.dominancePoints = dominancePoints;
    }
}