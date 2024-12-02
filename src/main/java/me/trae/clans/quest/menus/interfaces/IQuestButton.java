package me.trae.clans.quest.menus.interfaces;

import me.trae.clans.quest.Quest;
import me.trae.clans.quest.data.QuestData;

public interface IQuestButton {

    Quest getQuest();

    QuestData getData();
}