package me.trae.clans.clan.data;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.data.interfaces.IEnemy;

import java.util.Arrays;

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

    public Enemy(final String[] tokens) {
        this(tokens[0], Integer.parseInt(tokens[1]));
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

    @Override
    public String toString() {
        return String.join(":", Arrays.asList(this.getName(), String.valueOf(this.getDominancePoints())));
    }
}