package me.trae.clans.gamer;

import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.clans.gamer.interfaces.IGamer;
import me.trae.core.gamer.abstracts.AbstractGamer;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Gamer extends AbstractGamer<GamerProperty> implements IGamer {

    private int coins;

    public Gamer(final UUID uuid) {
        super(uuid);
    }

    public Gamer(final Player player) {
        this(player.getUniqueId());
    }

    public Gamer(final EnumData<GamerProperty> data) {
        super(data);

        this.coins = data.get(Integer.class, GamerProperty.COINS);
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
    public String getCoinsString() {
        return UtilString.toDollar(this.getCoins());
    }

    @Override
    public List<GamerProperty> getProperties() {
        return Arrays.asList(GamerProperty.values());
    }

    @Override
    public Object getValueByProperty(final GamerProperty property) {
        if (property == GamerProperty.COINS) {
            return this.getCoins();
        }

        return null;
    }
}