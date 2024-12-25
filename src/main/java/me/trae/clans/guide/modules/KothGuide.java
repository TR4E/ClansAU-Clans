package me.trae.clans.guide.modules;

import me.trae.clans.Clans;
import me.trae.clans.guide.GuideManager;
import me.trae.core.guide.Guide;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class KothGuide extends Guide<Clans, GuideManager> {

    public KothGuide(final GuideManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.BEACON);
    }

    @Override
    public String getDisplayName() {
        return "<light_purple><bold>KoTH";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "<dark_gray>KoTH (King of the Hill) is a competitive mode where",
                "<dark_gray>clans vie for control of a designated area in the center",
                "<dark_gray>of Fields.",
                "",
                "<gray>Through Territorial Battles, clans fight to dominate the hill",
                "<gray>and earn valuable rewards for maintaining control.",
                "",
                "<gray>Timed Events schedule these competitions, ensuring balanced",
                "<gray>participation and continuous engagement among players.",
                "",
                "<gray>Exclusive Rewards are granted to winning clans, including unique",
                "<gray>items and bonuses that enhance their status and capabilities.",
                "",
                "<gray>KoTH fosters intense competition and strategic gameplay, making",
                "<gray>it a favorite mode for clans aiming to prove their dominance."
        };
    }

}