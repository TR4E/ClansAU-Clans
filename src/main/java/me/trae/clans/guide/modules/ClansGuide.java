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
        return "Clans";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Inspired by the Factions Plugin.",
                "",
                "Clans enables players to form teams and work together to rise to the",
                "top of the server. Clans can establish powerful alliances or ignite fierce",
                "rivalries with one another.",
                "",
                "Alliances can form a mutual trust, granting both clans access to each other's doors and gates.",
                "",
                "What truly sets Clans apart is its method of territorial conquest. Instead of",
                "relying on raw power, Clans features a Dominance system, where outscoring an enemy clan",
                "in kills by a specific margin, grants the right to initiate an invasion. Both the invading",
                "clan and the dominated clan then have a brief period to prepare for battle. During the invasion",
                "the invading clan gains temporary access to the enemy's land, aiming to seize valuable resources",
                "and eliminate any defenders. Once the invasion concludes, the land returns to its protected state.",
                "",
                "Though nothing prevents another future campaign of domination."

        };
    }
}