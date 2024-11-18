package me.trae.clans.fields.modules;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.core.framework.types.frame.SpigotUpdater;
import me.trae.core.updater.annotations.Update;
import me.trae.core.utility.UtilTime;

public class HandleFieldsBlockUpdater extends SpigotUpdater<Clans, FieldsManager> {

    public HandleFieldsBlockUpdater(final FieldsManager manager) {
        super(manager);
    }

    @Update(delay = 250L)
    public void onUpdater() {
        if (!(UtilTime.elapsed(this.getManager().getLastUpdated(), this.getManager().duration))) {
            return;
        }

        this.getManager().replenish();

        this.getManager().updateLastUpdated();
    }
}