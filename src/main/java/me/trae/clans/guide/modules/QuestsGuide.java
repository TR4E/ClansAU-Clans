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
        return "Clans";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Quests enhance the gameplay experience by providing engaging objectives",
                "and rewarding players for their achievements.",
                "",
                "Daily Challenges:",
                "Every day, players receive a unique set of quests to complete,",
                "ensuring fresh and varied goals. Daily tasks include activities such as:",
                "- Killing 5 Assassins or Knights.",
                "- Mining 10 different ores at Fields.",
                "- Catching 15 fish at the Lake.",
                "",
                "Upon completing each quest, players earn valuable rewards,",
                "including items, experience points, and exclusive bonuses.",
                "",
                "These daily quests encourage consistent participation,",
                "allowing players to progress and gain rewards through regular",
                "engagement and diverse challenges."
        };
    }

}