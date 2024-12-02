package me.trae.clans.fields;

import me.trae.clans.Clans;
import me.trae.clans.config.Config;
import me.trae.clans.fields.data.FieldsBlock;
import me.trae.clans.fields.data.enums.FieldsBlockProperty;
import me.trae.core.database.query.Query;
import me.trae.core.database.query.types.DeleteQuery;
import me.trae.core.database.query.types.MultiCallbackQuery;
import me.trae.core.database.query.types.SaveQuery;
import me.trae.core.database.query.types.UpdateQuery;
import me.trae.core.database.repository.Repository;
import me.trae.core.database.repository.types.ContainsRepository;
import me.trae.core.database.repository.types.MultiLoadRepository;
import me.trae.core.database.repository.types.UpdateRepository;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.objects.EnumData;
import org.bukkit.Location;

public class FieldsRepository extends Repository<Clans, FieldsManager, Config> implements ContainsRepository<Location>, UpdateRepository<FieldsBlock, FieldsBlockProperty>, MultiLoadRepository {

    public FieldsRepository(final FieldsManager manager) {
        super(manager, "Fields");
    }

    @Override
    public Class<Config> getClassOfConfiguration() {
        return Config.class;
    }

    @Override
    public boolean containsData(final Location location) {
        return this.getConfig().getYamlConfiguration().contains(UtilLocation.locationToFile(location));
    }

    @Override
    public void saveData(final FieldsBlock fieldsBlock) {
        final SaveQuery<FieldsBlockProperty> query = new SaveQuery<FieldsBlockProperty>() {
            @Override
            public String getKey() {
                return UtilBlock.locationToFile(fieldsBlock.getLocation());
            }

            @Override
            public EnumData<FieldsBlockProperty> getData() {
                return fieldsBlock.getData();
            }
        };

        this.addQuery(query);
    }

    @Override
    public void updateData(final FieldsBlock fieldsBlock, final FieldsBlockProperty property) {
        final UpdateQuery<FieldsBlockProperty> query = new UpdateQuery<FieldsBlockProperty>() {
            @Override
            public String getKey() {
                return UtilBlock.locationToFile(fieldsBlock.getLocation());
            }

            @Override
            public FieldsBlockProperty getProperty() {
                return property;
            }

            @Override
            public Object getValue() {
                return fieldsBlock.getValueByProperty(property);
            }
        };

        this.addQuery(query);
    }

    @Override
    public void deleteData(final FieldsBlock fieldsBlock) {
        final DeleteQuery query = new DeleteQuery() {
            @Override
            public String getKey() {
                return UtilBlock.locationToFile(fieldsBlock.getLocation());
            }
        };

        this.addQuery(query);
    }

    @Override
    public void loadData() {
        final MultiCallbackQuery<FieldsBlockProperty> query = new MultiCallbackQuery<FieldsBlockProperty>() {
            @Override
            public void onCallback(final EnumData<FieldsBlockProperty> data) {
                final FieldsBlock fieldsBlock = new FieldsBlock(data);

                getManager().addBlock(fieldsBlock);

                incrementCount();
            }
        };

        this.addQuery(query);
    }

    @Override
    public boolean isInform(final Query query) {
        return query instanceof MultiCallbackQuery<?>;
    }
}