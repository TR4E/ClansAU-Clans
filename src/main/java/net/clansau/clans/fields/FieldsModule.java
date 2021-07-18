package net.clansau.clans.fields;

import net.clansau.core.framework.Module;

public class FieldsModule extends Module<FieldsManager> {

    public FieldsModule(final FieldsManager manager) {
        super(manager, "Fields Module");
    }

    @Override
    protected void initializeModule() {
        getManager().setupSavedBlocks();
    }

    @Override
    protected void shutdownModule() {
        getManager().restore(true);
    }
}