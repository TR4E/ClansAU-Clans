package net.clansau.clans.config;

import net.clansau.clans.Clans;
import net.clansau.core.config.framework.IOptionsManager;
import org.bukkit.configuration.file.YamlConfiguration;

public class OptionsManager extends IOptionsManager {

    public OptionsManager(final Clans instance) {
        super(instance);
    }

    @Override
    protected void handleConfig(final String type, final YamlConfiguration yml) {
        switch (type) {
            case "Config": {
                addData("Clans.Pillage-Length", yml.getInt("Clans.Pillage-Length"));
            }
        }
    }

    public final int getClansPillageLength() {
        return getData("Clans.Pillage-Length");
    }
}