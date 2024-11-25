package me.trae.clans.weapon.weapons.items.data.interfaces;

import me.trae.core.utility.components.time.ExpiredComponent;
import me.trae.core.utility.components.time.GetDurationComponent;
import me.trae.core.utility.components.time.GetSystemTimeComponent;
import me.trae.core.utility.components.time.RemainingComponent;
import org.bukkit.Location;

public interface ISupplyCrateData extends GetSystemTimeComponent, GetDurationComponent, RemainingComponent, ExpiredComponent {

    Location getLocation();

    int getCount();

    boolean isFilled();

    void setFilled(final boolean filled);
}