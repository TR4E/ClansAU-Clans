package me.trae.clans.config;

import me.trae.clans.Clans;
import me.trae.core.config.abstracts.AbstractConfig;
import me.trae.core.config.abstracts.AbstractConfigManager;

public class ConfigManager extends AbstractConfigManager<Clans> {

    public ConfigManager(final Clans instance) {
        super(instance);
    }

    @Override
    public AbstractConfig createConfig(final String name) {
        return new Config(name);
    }
}