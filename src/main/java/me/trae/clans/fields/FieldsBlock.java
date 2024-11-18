package me.trae.clans.fields;

import me.trae.clans.fields.enums.FieldsBlockProperty;
import me.trae.clans.fields.interfaces.IFieldsBlock;
import me.trae.core.blockrestore.data.RestoreData;
import me.trae.core.database.containers.DataContainer;
import me.trae.core.database.query.constants.DefaultProperty;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.List;

public class FieldsBlock implements IFieldsBlock, DataContainer<FieldsBlockProperty> {

    private final Location location;
    private final RestoreData oldRestoreData;

    private boolean broken;

    private FieldsBlock(final Location location, final RestoreData oldRestoreData, final boolean broken) {
        this.location = location;
        this.oldRestoreData = oldRestoreData;
        this.broken = broken;
    }

    public FieldsBlock(final Block block) {
        this(block.getLocation(), new RestoreData(block.getType(), block.getData()), false);
    }

    public FieldsBlock(final EnumData<FieldsBlockProperty> data) {
        this(UtilLocation.fileToLocation(data.get(String.class, DefaultProperty.KEY)), new RestoreData(data.get(String.class, FieldsBlockProperty.OLD_RESTORE).split(":")), data.get(Boolean.class, FieldsBlockProperty.BROKEN));
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public RestoreData getOldRestoreData() {
        return this.oldRestoreData;
    }

    @Override
    public boolean isBroken() {
        return this.broken;
    }

    @Override
    public void setBroken(final boolean broken) {
        this.broken = broken;
    }

    @Override
    public void update(final FieldsManager manager) {
        final RestoreData restoreData = manager.getNewRestoreDataByMaterial(this.getOldRestoreData().getMaterial());

        restoreData.update(this.getLocation());
    }

    @Override
    public void restore() {
        this.getOldRestoreData().update(this.getLocation());
    }

    @Override
    public List<FieldsBlockProperty> getProperties() {
        return Arrays.asList(FieldsBlockProperty.values());
    }

    @Override
    public Object getValueByProperty(final FieldsBlockProperty property) {
        switch (property) {
            case OLD_RESTORE:
                return this.getOldRestoreData().toString();
            case BROKEN:
                return this.isBroken();
        }

        return null;
    }
}