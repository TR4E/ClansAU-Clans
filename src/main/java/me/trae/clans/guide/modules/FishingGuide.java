package me.trae.clans.guide.modules;

import me.trae.clans.Clans;
import me.trae.clans.guide.GuideManager;
import me.trae.core.guide.Guide;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FishingGuide extends Guide<Clans, GuideManager> {

    public FishingGuide(final GuideManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.FISHING_ROD);
    }

    @Override
    public String getDisplayName() {
        return "<white><bold>Fishing";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "<dark_gray>Lake is a fishing spot located at Fields, where players can cast",
                "<dark_gray>their lines and reel in fish, each uniquely weighted in pounds.",
                "",
                "<gray>Reeling in a catch might yield anything from common fish to rare items",
                "<gray>lost by fishermen long ago.",
                "",
                "<gray>This watery zone can be surprisingly dangerous, as there's always a chance",
                "<gray>to hook a hostile mob instead. Despite the risks, its unpredictable rewards",
                "<gray>make the Lake a hotspot for anglers seeking bigger thrills.",
                "",
                "<gray>During the <green>Fishing Frenzy<gray> world event, fishing times are much",
                "<gray>faster and the odds of obtaining rare items are greatly increased, making",
                "<gray>the Lake even more enticing for adventurers."
        };
    }

}