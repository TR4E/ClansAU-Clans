package me.trae.clans.gamer.interfaces;

public interface IGamer {

    int getCoins();

    void setCoins(final int coins);

    boolean hasCoins(final int coins);

    String getCoinsString();

    long getProtection();

    void setProtection(final long protection);

    void addProtection(final long duration);

    void takeProtection(final long duration);

    boolean hasProtection();
}