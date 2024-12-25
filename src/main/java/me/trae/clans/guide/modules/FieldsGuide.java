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
        return "<white><bold>Fields";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "<dark_gray>Fields is a massive area located at the center of the map, where players",
                "<dark_gray>can gather to mine ores and discover loot boxes. Mining these ores yields",
                "<dark_gray>smelted gems that can be used for various upgrades or trades.",
                "",
                "<gray>This zone is a known PvP hotspot, making it extremely dangerous for",
                "<gray>unprepared players.",
                "",
                "<gray>Surprisingly, armor does not lose durability when taking damage here,",
                "<gray>allowing for prolonged fights.",
                "",
                "<gray>With its high rewards and fierce competition, Fields stands out as a hub",
                "<gray>for thrill-seekers looking for more competitive action.",
                "",
                "<gray>Especially during the <bold>Mining Madness<gray> world event, when players",
                "<gray>can earn double the loot and enjoy increased chances of finding rare items.",
                "",
                "<gray>Every mined ore/block in fields, regenerate over time."
        };
    }

}