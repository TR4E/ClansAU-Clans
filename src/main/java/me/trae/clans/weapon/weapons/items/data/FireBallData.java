package me.trae.clans.weapon.weapons.items.data;

import me.trae.clans.weapon.weapons.items.data.interfaces.IFireBallData;
import me.trae.core.weapon.data.WeaponData;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FireBallData extends WeaponData implements IFireBallData {

    private final List<LargeFireball> fireballs;

    public FireBallData(final Player player, final long duration) {
        super(player, duration);

        this.fireballs = new ArrayList<>();
    }

    @Override
    public List<LargeFireball> getFireballs() {
        return this.fireballs;
    }

    @Override
    public void addFireball(final LargeFireball fireball) {
        this.getFireballs().add(fireball);
    }
}