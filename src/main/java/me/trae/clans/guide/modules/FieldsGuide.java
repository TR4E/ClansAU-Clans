package me.trae.clans.guide.modules;

import me.trae.clans.Clans;
import me.trae.clans.guide.GuideManager;
import me.trae.core.guide.Guide;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FieldsGuide extends Guide<Clans, GuideManager> {

    public FieldsGuide(final GuideManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.IRON_PICKAXE);
    }

    @Override
    public String getDisplayName() {
        return "Fields";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Fields is a massive area located at the center of the map, where players",
                "can gather to mine ores and discover loot boxes. Mining these ores yields",
                "smelted gems that can be used for various upgrades or trades.",
                "",
                "This zone is a known PvP hotspot, making it extremely dangerous for",
                "unprepared players.",
                "",
                "Surprisingly, armor does not lose durability when taking damage here,",
                "allowing for prolonged fights.",
                "",
                "With its high rewards and fierce competition, Fields stands out as a hub",
                "for thrill-seekers looking for more competitive action.",
                "",
                "Especially during the <bold>Mining Madness</bold> world event, when players",
                "can earn double the loot and enjoy increased chances of finding rare items.",
                "",
                "Every mined ore/block in fields, regenerate over time."
        };
    }

}