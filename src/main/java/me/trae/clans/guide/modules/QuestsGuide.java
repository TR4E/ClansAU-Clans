package me.trae.clans.guide.modules;

import me.trae.clans.Clans;
import me.trae.clans.guide.GuideManager;
import me.trae.core.guide.Guide;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class QuestsGuide extends Guide<Clans, GuideManager> {

    public QuestsGuide(final GuideManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.GOLD_SWORD);
    }

    @Override
    public String getDisplayName() {
        return "<gold><bold>Quests";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "<dark_gray>Quests enhance the gameplay experience by providing engaging objectives",
                "<dark_gray>and rewarding players for their achievements.",
                "",
                "<white>Daily Challenges:",
                "<gray>Every day, players receive a unique set of quests to complete,",
                "<gray>ensuring fresh and varied goals. Daily tasks include activities such as:",
                "<gold>• Killing 5 Assassins or Knights.",
                "<gold>• Mining 10 different ores at Fields.",
                "<gold>• Catching 15 fish at the Lake.",
                "",
                "<gray>Upon completing each quest, players earn valuable rewards,",
                "<gray>including items, experience points, and exclusive bonuses.",
                "",
                "<gray>These daily quests encourage consistent participation,",
                "<gray>allowing players to progress and gain rewards through regular",
                "<gray>engagement and diverse challenges."
        };
    }

}