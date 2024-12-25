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
        return "<red><bold>TNT Mechanics";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "<dark_gray>Custom TNT Mechanics enhance explosions with unique effects and",
                "<dark_gray>transformations, adding depth to gameplay.",
                "",
                "<white>Block Transformations:",
                "<gray>When TNT detonates, affected blocks undergo a series of",
                "<gray>transformations to create a cracked effect before being removed:",
                "<gold>• Stone Brick → Cracked Stone Brick → Air",
                "<gold>• Nether Brick → Netherrack → Air",
                "<gold>• Quartz Block → Chiseled Quartz Block → Air",
                "<gold>• Smooth Sandstone → Normal Sandstone → Air",
                "<gold>• Smooth Red Sandstone → Normal Red Sandstone → Air",
                "<gold>• Dark Prismarine → Prismarine Bricks → Prismarine → Air",
                "",
                "<white>Liquid Removal:",
                "<gray>Explosions effectively remove all liquids within the blast radius,",
                "<gray>altering the environment and strategic dynamics.",
                "",
                "<gray>These custom mechanics provide both aesthetic enhancements and",
                "<gray>tactical advantages, making TNT usage more engaging and impactful",
                "<gray>for players."
        };
    }

}