package me.trae.clans.clan;

import me.trae.clans.Clans;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.types.AdminClan;
import me.trae.clans.config.Config;
import me.trae.core.database.query.types.DeleteQuery;
import me.trae.core.database.query.types.MultiCallbackQuery;
import me.trae.core.database.query.types.SaveQuery;
import me.trae.core.database.query.types.UpdateQuery;
import me.trae.core.database.repository.Repository;
import me.trae.core.database.repository.types.ContainsRepository;
import me.trae.core.database.repository.types.MultiLoadRepository;
import me.trae.core.database.repository.types.UpdateRepository;
import me.trae.core.utility.objects.EnumData;

public class ClanRepository extends Repository<Clans, ClanManager, Config> implements ContainsRepository<String>, UpdateRepository<Clan, ClanProperty>, MultiLoadRepository {

    public ClanRepository(final ClanManager manager) {
        super(manager, "Clans");
    }

    @Override
    public Class<Config> getClassOfConfiguration() {
        return Config.class;
    }

    @Override
    public boolean containsData(final String name) {
        return this.getConfig().getYamlConfiguration().contains(name);
    }

    @Override
    public void saveData(final Clan clan) {
        final SaveQuery<ClanProperty> query = new SaveQuery<ClanProperty>() {
            @Override
            public String getKey() {
                return clan.getName();
            }

            @Override
            public EnumData<ClanProperty> getData() {
                return clan.getData();
            }
        };

        this.addQuery(query);
    }

    @Override
    public void updateData(final Clan clan, final ClanProperty property) {
        final UpdateQuery<ClanProperty> query = new UpdateQuery<ClanProperty>() {
            @Override
            public String getKey() {
                return clan.getName();
            }

            @Override
            public ClanProperty getProperty() {
                return property;
            }

            @Override
            public Object getValue() {
                return clan.getValueByProperty(property);
            }
        };

        this.addQuery(query);
    }

    @Override
    public void deleteData(final Clan clan) {
        final DeleteQuery query = new DeleteQuery() {
            @Override
            public String getKey() {
                return clan.getName();
            }
        };

        this.addQuery(query);
    }

    @Override
    public void loadData() {
        final MultiCallbackQuery<ClanProperty> query = new MultiCallbackQuery<ClanProperty>() {
            @Override
            public void onCallback(final EnumData<ClanProperty> data) {
                final Clan clan = data.get(Boolean.class, ClanProperty.ADMIN) ? new AdminClan(data) : new Clan(data);

                getManager().addClan(clan);

                incrementCount();
            }
        };

        this.addQuery(query);
    }
}