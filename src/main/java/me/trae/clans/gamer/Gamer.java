package me.trae.clans.gamer;

import me.trae.clans.Clans;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.clans.gamer.interfaces.IGamer;
import me.trae.core.gamer.abstracts.AbstractGamer;
import me.trae.core.utility.UtilPlugin;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Gamer extends AbstractGamer<GamerProperty> implements IGamer {

    private int coins;
    private long protection;

    public Gamer(final UUID uuid) {
        super(uuid);
    }

    public Gamer(final Player player) {
        this(player.getUniqueId());

        final GamerManager gamerManager = UtilPlugin.getInstanceByClass(Clans.class).getManagerByClass(GamerManager.class);

        this.coins = gamerManager.starterCoinsAmount;
        this.protection = gamerManager.starterProtectionDuration;
    }

    public Gamer(final EnumData<GamerProperty> data) {
        super(data);

        this.coins = data.get(Integer.class, GamerProperty.COINS, 0);
        this.protection = data.get(Long.class, GamerProperty.PROTECTION, 0L);
    }

    @Override
    public int getCoins() {
        return this.coins;
    }

    @Override
    public void setCoins(final int coins) {
        this.coins = coins;
    }

    @Override
    public boolean hasCoins(final int coins) {
        return this.getCoins() >= coins;
    }

    @Override
    public String getCoinsString() {
        return UtilString.toDollar(this.getCoins());
    }

    @Override
    public long getProtection() {
        return this.protection;
    }

    @Override
    public void setProtection(final long protection) {
        this.protection = protection;
    }

    @Override
    public void addProtection(final long duration) {
        this.setProtection(this.getProtection() + duration);
    }

    @Override
    public void takeProtection(final long duration) {
        this.setProtection(Math.max(0L, this.getProtection() - duration));
    }

    @Override
    public boolean hasProtection() {
        return this.getProtection() > 0L;
    }

    @Override
    public List<GamerProperty> getProperties() {
        return Arrays.asList(GamerProperty.values());
    }

    @Override
    public Object getValueByProperty(final GamerProperty property) {
        switch (property) {
            case COINS:
                return this.getCoins();
            case PROTECTION:
                return this.getProtection();
        }

        return null;
    }
}