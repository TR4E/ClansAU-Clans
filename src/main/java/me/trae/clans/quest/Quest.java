package me.trae.clans.quest;

import me.trae.clans.Clans;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.quest.data.QuestData;
import me.trae.clans.quest.data.enums.QuestDataProperty;
import me.trae.clans.quest.interfaces.IQuest;
import me.trae.clans.quest.models.CoinsQuestReward;
import me.trae.clans.quest.models.ItemQuestReward;
import me.trae.core.framework.SpigotModule;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Quest extends SpigotModule<Clans, QuestManager> implements IQuest {

    private final Map<UUID, QuestData> USERS = new HashMap<>();

    public Quest(final QuestManager manager) {
        super(manager);
    }

    @Override
    public Map<UUID, QuestData> getUsers() {
        return this.USERS;
    }

    @Override
    public void addUser(final QuestData questData) {
        this.getUsers().put(questData.getUUID(), questData);
    }

    @Override
    public void removeUser(final Player player) {
        this.getUsers().remove(player.getUniqueId());
    }

    @Override
    public QuestData getUserByPlayer(final Player player) {
        return this.getUsers().getOrDefault(player.getUniqueId(), null);
    }

    @Override
    public boolean isUserByPlayer(final Player player) {
        return this.getUsers().containsKey(player.getUniqueId());
    }

    @Override
    public String getDisplayName() {
        return this.getName().replace(" ", String.format(" %s ", this.getMaxProgress()));
    }

    @Override
    public void addProgress(final Player player) {
        QuestData data = this.getUserByPlayer(player);
        if (data == null) {
            data = new QuestData(this, player);
            this.addUser(data);
            this.getManager().getRepository().saveData(data);
        }

        if (data.getProgress() == this.getMaxProgress()) {
            return;
        }

        data.addProgress();
        this.getManager().getRepository().updateData(data, QuestDataProperty.PROGRESS);

        if (data.getProgress() == this.getMaxProgress()) {
            this.reward(player);
            return;
        }
    }

    @Override
    public void reward(final Player player) {
        if (this instanceof CoinsQuestReward) {
            final int coins = UtilJava.cast(CoinsQuestReward.class, this).getCoins();

            this.getInstanceByClass().getManagerByClass(GamerManager.class).giveCoins(player, coins);
        }

        if (this instanceof ItemQuestReward) {
            final ItemStack itemStack = UtilJava.cast(ItemQuestReward.class, this).getItemStack();

            UtilItem.insert(player, itemStack);
        }

        UtilMessage.simpleMessage(player, "Quest", "You have completed <green><var></green> and received <var>.", Arrays.asList(this.getDisplayName(), this.getRewardString()));
    }
}