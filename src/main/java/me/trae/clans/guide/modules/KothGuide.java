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
        return "KoTH";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "KoTH (King of the Hill) is a competitive mode where",
                "clans vie for control of a designated area in the center",
                "of Fields.",
                "",
                "Through Territorial Battles, clans fight to dominate the hill",
                "and earn valuable rewards for maintaining control.",
                "",
                "Timed Events schedule these competitions, ensuring balanced",
                "participation and continuous engagement among players.",
                "",
                "Exclusive Rewards are granted to winning clans, including unique",
                "items and bonuses that enhance their status and capabilities.",
                "",
                "KoTH fosters intense competition and strategic gameplay, making",
                "it a favorite mode for clans aiming to prove their dominance."
        };
    }

}