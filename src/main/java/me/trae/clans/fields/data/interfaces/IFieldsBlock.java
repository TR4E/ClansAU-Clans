package me.trae.clans.fields.data.interfaces;

import me.trae.clans.fields.FieldsManager;
import me.trae.core.blockrestore.data.RestoreData;
import org.bukkit.Location;

public interface IFieldsBlock {

    Location getLocation();

    RestoreData getOldRestoreData();

    boolean isBroken();

    void setBroken(final boolean broken);

    void update(final FieldsManager manager);

    void restore();
}