package me.trae.clans.weapon.weapons.items.data.interfaces;

import me.trae.core.utility.components.ExpiredComponent;
import me.trae.core.utility.components.GetDurationComponent;
import me.trae.core.utility.components.GetSystemTimeComponent;
import me.trae.core.utility.components.RemainingComponent;
import org.bukkit.Location;

public interface ISupplyCrateData extends GetSystemTimeComponent, GetDurationComponent, RemainingComponent, ExpiredComponent {

    Location getLocation();

    int getCount();

    boolean isFilled();

    void setFilled(final boolean filled);
}