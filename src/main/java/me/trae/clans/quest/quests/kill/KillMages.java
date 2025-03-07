package me.trae.clans.quest.quests.kill;

import me.trae.api.champions.role.Role;
import me.trae.api.champions.role.events.RoleCheckEvent;
import me.trae.clans.quest.Quest;
import me.trae.clans.quest.QuestManager;
import me.trae.clans.quest.models.types.KillQuest;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import org.bukkit.entity.Player;

public class KillMages extends Quest implements KillQuest<Player> {

    public KillMages(final QuestManager manager) {
        super(manager);
    }

    @Override
    public Class<Player> getClassOfEntity() {
        return Player.class;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "To complete this quest, you",
                UtilString.format("have to <gold>%s</gold>", this.getDisplayName())
        };
    }

    @Override
    public int getMaxProgress() {
        return 5;
    }

    @Override
    public int getCoins() {
        return 10_000;
    }

    @Override
    public boolean canKill(final Player killer, final Player entity) {
        final Role role = UtilServer.getEvent(new RoleCheckEvent(entity)).getRole();
        if (role == null) {
            return false;
        }

        return role.getName().equals("Mage");
    }
}