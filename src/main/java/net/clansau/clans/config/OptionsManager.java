package net.clansau.clans.config;

import net.clansau.core.config.Config;
import net.clansau.core.config.framework.IOptionsManager;
import net.clansau.core.framework.Plugin;

public class OptionsManager extends IOptionsManager {

    public OptionsManager(final Plugin instance) {
        super(instance);
    }

    @Override
    protected void handleConfig(final String type, final Config config) {
    }
}