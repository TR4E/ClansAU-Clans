package me.trae.clans.guide.modules;

import me.trae.clans.Clans;
import me.trae.clans.guide.GuideManager;
import me.trae.core.guide.Guide;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TntMechanicsGuide extends Guide<Clans, GuideManager> {

    public TntMechanicsGuide(final GuideManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.TNT);
    }

    @Override
    public String getDisplayName() {
        return "TNT Mechanics";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Custom TNT Mechanics enhance explosions with unique effects and",
                "transformations, adding depth to gameplay.",
                "",
                "Block Transformations:",
                "When TNT detonates, affected blocks undergo a series of",
                "transformations to create a cracked effect before being removed:",
                "- Stone Brick → Cracked Stone Brick → Air",
                "- Nether Brick → Netherrack → Air",
                "- Quartz Block → Chiseled Quartz Block → Air",
                "- Smooth Sandstone → Normal Sandstone → Air",
                "- Smooth Red Sandstone → Normal Red Sandstone → Air",
                "- Dark Prismarine → Prismarine Bricks → Prismarine → Air",
                "",
                "Liquid Removal:",
                "Explosions effectively remove all liquids within the blast radius,",
                "altering the environment and strategic dynamics.",
                "",
                "These custom mechanics provide both aesthetic enhancements and",
                "tactical advantages, making TNT usage more engaging and impactful",
                "for players."
        };
    }

}