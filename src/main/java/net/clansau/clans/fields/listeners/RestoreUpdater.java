package net.clansau.clans.fields.listeners;

import net.clansau.clans.fields.FieldsManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.framework.updater.UpdateEvent;
import net.clansau.core.framework.updater.Updater;
import org.bukkit.event.EventHandler;

public class RestoreUpdater extends CoreListener<FieldsManager> {

    public RestoreUpdater(final FieldsManager manager) {
        super(manager, "Restore Updater");
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (!(e.getType().equals(Updater.UpdateType.FAST))) {
            return;
        }
        getManager().restore(false);
    }
}