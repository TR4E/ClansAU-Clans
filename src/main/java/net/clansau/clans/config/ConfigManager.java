package net.clansau.clans.config;

import net.clansau.clans.Clans;
import net.clansau.core.config.Config;
import net.clansau.core.config.OptionsManager;
import net.clansau.core.config.framework.IConfigManager;
import net.clansau.core.config.framework.IOptionsManager;

public class ConfigManager extends IConfigManager {

    public ConfigManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void handleConfig(final String type, final Config config) {

    }

    @Override
    protected String[] getConfigTypes() {
        return new String[0];
    }

    @Override
    public IOptionsManager getOptionsManager() {
        return getInstance().getManager(OptionsManager.class);
    }
}