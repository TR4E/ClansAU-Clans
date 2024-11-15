package me.trae.clans.clan.types;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.types.interfaces.IAdminClan;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.entity.Player;

public class AdminClan extends Clan implements IAdminClan {

    private boolean safe;

    public AdminClan(final String name) {
        super(name);
    }

    public AdminClan(final String name, final Player player) {
        super(name, player);
    }

    public AdminClan(final EnumData<ClanProperty> data) {
        super(data);

        this.safe = data.get(Boolean.class, ClanProperty.SAFE);
    }

    @Override
    public String getType() {
        return "Admin Clan";
    }

    @Override
    public boolean isSafe() {
        return this.safe;
    }

    @Override
    public void setSafe(final boolean safe) {
        this.safe = safe;
    }
}