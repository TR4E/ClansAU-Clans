package net.clansau.clans.config;

import net.clansau.clans.Clans;
import net.clansau.core.config.OptionsManager;
import net.clansau.core.config.framework.IConfigManager;
import net.clansau.core.config.framework.IOptionsManager;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager extends IConfigManager {

    public ConfigManager(final Clans instance) {
        super(instance);
    }

    @Override
    protected void handleConfig(final String type, final YamlConfiguration yml) {
        switch (type) {
            case "Config": {
                yml.set("Clans.Pillage-Length", 10);
                yml.set("Clans.Name-Length.Max", 14);
                yml.set("Clans.Name-Length.Min", 3);
                yml.set("Clans.Max-Clan-Members", 8);
            }
        }
    }

    @Override
    protected final String[] getTypes() {
        return new String[0];
    }

    @Override
    public final IOptionsManager getOptionsManager() {
        return getInstance().getManager(OptionsManager.class);
    }
}