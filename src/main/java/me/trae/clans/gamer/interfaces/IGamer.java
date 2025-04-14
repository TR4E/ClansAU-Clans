package me.trae.clans.gamer.interfaces;

public interface IGamer {

    int getCoins();

    void setCoins(final int coins);

    boolean hasCoins(final int coins);

    String getCoinsString();

    int getKills();

    void setKills(final int kills);

    int getDeaths();

    void setDeaths(final int deaths);

    int getBlocksBroken();

    void setBlocksBroken(final int blocksBroken);

    int getBlocksPlaced();

    void setBlocksPlaced(final int blocksPlaced);

    long getProtection();

    void setProtection(final long protection);

    void addProtection(final long duration);

    void takeProtection(final long duration);

    boolean hasProtection();
}