package me.trae.clans.guide.modules;

import me.trae.clans.Clans;
import me.trae.clans.guide.GuideManager;
import me.trae.core.guide.Guide;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ClansGuide extends Guide<Clans, GuideManager> {

    public ClansGuide(final GuideManager manager) {
        super(manager);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.IRON_DOOR);
    }

    @Override
    public String getDisplayName() {
        return "<aqua><bold>Clans";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "<dark_gray>Inspired by the Factions Plugin.",
                "",
                "<dark_gray>Clans enables players to form teams and work together to rise to the",
                "<dark_gray>top of the server. Clans can establish powerful alliances or ignite fierce",
                "<dark_gray>rivalries with one another.",
                "",
                "<gray>Alliances can form a mutual trust, granting both clans access to each other's doors and gates.",
                "",
                "<gray>What truly sets Clans apart is its method of territorial conquest. Instead of",
                "<gray>relying on raw power, Clans features a Dominance system, where outscoring an enemy clan",
                "<gray>in kills by a specific margin, grants the right to initiate an invasion. ",
                "<gray>Both the invading clan and the dominated clan then have a brief period to prepare for battle.",
                "",
                "<gray>During the invasion the invading clan gains temporary access to the enemy's land, aiming to seize valuable resources",
                "<gray>and eliminate any defenders. Once the invasion concludes, the land returns to its protected state.",
                "",
                "<gray>Though nothing prevents another future campaign of domination."

        };
    }
}