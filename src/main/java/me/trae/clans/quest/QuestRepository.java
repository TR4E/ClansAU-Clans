package me.trae.clans.quest;

import me.trae.clans.Clans;
import me.trae.clans.quest.data.QuestData;
import me.trae.clans.quest.data.enums.QuestDataProperty;
import me.trae.core.database.query.Query;
import me.trae.core.database.query.types.DeleteQuery;
import me.trae.core.database.query.types.SaveQuery;
import me.trae.core.database.query.types.SingleCallbackQuery;
import me.trae.core.database.query.types.UpdateQuery;
import me.trae.core.database.repository.Repository;
import me.trae.core.database.repository.types.ContainsRepository;
import me.trae.core.database.repository.types.SingleLoadRepository;
import me.trae.core.database.repository.types.UpdateRepository;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.objects.EnumData;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class QuestRepository extends Repository<Clans, QuestManager> implements ContainsRepository<UUID>, UpdateRepository<QuestData, QuestDataProperty>, SingleLoadRepository<UUID> {

    public QuestRepository(final QuestManager manager) {
        super(manager, "Quests");
    }

    @Override
    public boolean containsData(final UUID uuid) {
        return this.containsIdentifier(uuid.toString());
    }

    @Override
    public void saveData(final QuestData questData) {
        final SaveQuery<QuestDataProperty> query = new SaveQuery<QuestDataProperty>() {
            @Override
            public String getKey() {
                return questData.getUUID().toString();
            }

            @Override
            public List<String> getTypes() {
                return Collections.singletonList(questData.getType());
            }

            @Override
            public EnumData<QuestDataProperty> getData() {
                return questData.getData();
            }
        };

        this.addQuery(query);
    }

    @Override
    public void updateData(final QuestData questData, final QuestDataProperty property) {
        final UpdateQuery<QuestDataProperty> query = new UpdateQuery<QuestDataProperty>() {
            @Override
            public String getKey() {
                return questData.getUUID().toString();
            }

            @Override
            public List<String> getTypes() {
                return Collections.singletonList(questData.getType());
            }

            @Override
            public QuestDataProperty getProperty() {
                return property;
            }

            @Override
            public Object getValue() {
                return questData.getValueByProperty(property);
            }
        };

        this.addQuery(query);
    }

    @Override
    public void deleteData(final QuestData questData) {
        final DeleteQuery query = new DeleteQuery() {
            @Override
            public String getKey() {
                return questData.getUUID().toString();
            }

            @Override
            public List<String> getTypes() {
                return Collections.singletonList(questData.getType());
            }
        };

        this.addQuery(query);
    }

    @Override
    public void loadData(final UUID uuid) {
        final SingleCallbackQuery<QuestDataProperty> query = new SingleCallbackQuery<QuestDataProperty>() {
            @Override
            public String getKey() {
                return uuid.toString();
            }

            @Override
            public void onCallback(final EnumData<QuestDataProperty> data) {
                final QuestData questData = new QuestData(data);

                final Quest quest = UtilJava.cast(Quest.class, getManager().getModuleByName(questData.getType()));
                if (quest == null) {
                    return;
                }

                quest.addUser(questData);
            }

            @Override
            public int getTypeIndex() {
                return 1;
            }
        };

        this.addQuery(query);
    }

    @Override
    public boolean isInform(final Query query) {
        return query instanceof SingleCallbackQuery<?>;
    }
}