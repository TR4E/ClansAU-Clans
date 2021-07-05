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
                addData("Clans.Name-Length.Max", yml.getInt("Clans.Name-Length.Max"));
                addData("Clans.Name-Length.Min", yml.getInt("Clans.Name-Length.Min"));
                addData("Clans.Max-Clan-Members", yml.getInt("Clans.Max-Clan-Members"));
            }
        }
    }

    public final int getClansPillageLength() {
        return getData("Clans.Pillage-Length");
    }

    public final int getClansMaxLengthName() {
        return getData("Clans.Name-Length.Max");
    }

    public final int getClansMinLengthName() {
        return getData("Clans.Name-Length.Min");
    }

    public final int getClansMaxMembers() {
        return getData("Clans.Max-Clan-Members");
    }
}