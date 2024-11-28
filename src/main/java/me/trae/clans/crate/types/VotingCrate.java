package me.trae.clans.crate.types;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.CrateManager;
import me.trae.clans.crate.loot.types.TntLoot;
import me.trae.clans.crate.loot.types.coins.FifteenThousandCoinsLoot;
import me.trae.clans.crate.loot.types.coins.OneHundredThousandCoinsLoot;
import me.trae.clans.crate.loot.types.coins.TenThousandCoinsLoot;
import me.trae.clans.crate.loot.types.gems.*;
import me.trae.clans.crate.loot.types.tools.DiamondAxeLoot;
import me.trae.clans.crate.loot.types.tools.DiamondPickaxeLoot;
import me.trae.clans.crate.loot.types.tools.DiamondSpadeLoot;
import me.trae.clans.crate.loot.types.tools.DiamondSwordLoot;
import me.trae.clans.crate.loot.types.weapons.*;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VotingCrate extends Crate {

    public VotingCrate(final CrateManager manager) {
        super(manager, new ItemStack(Material.CHEST));
    }

    @Override
    public void registerSubModules() {
        // Coins Loot
        addSubModule(new OneHundredThousandCoinsLoot(this, 5.0D));
        addSubModule(new FifteenThousandCoinsLoot(this, 50.0D));
        addSubModule(new TenThousandCoinsLoot(this, 120.0D));

        // Gems Loot
        addSubModule(new DiamondLoot(this, 50.0D));
        addSubModule(new EmeraldLoot(this, 50.0D));
        addSubModule(new LeatherLoot(this, 50.0D));
        addSubModule(new GoldIngotLoot(this, 45.0D));
        addSubModule(new IronIngotLoot(this, 45.0D));

        // Tools Loot
        addSubModule(new DiamondSpadeLoot(this, 35.0D));
        addSubModule(new DiamondPickaxeLoot(this, 35.0D));
        addSubModule(new DiamondSwordLoot(this, 10.0D));
        addSubModule(new DiamondAxeLoot(this, 10.0D));

        // Other Loot
        addSubModule(new TntLoot(this, 4.0D));

        // Legendaries Loot
        addSubModule(new AlligatorsToothLoot(this, 0.06D));
        addSubModule(new DwarvenPickaxeLoot(this, 0.06D));
        addSubModule(new GiantsBroadswordLoot(this, 0.06D));
        addSubModule(new HyperAxeLoot(this, 0.06D));
        addSubModule(new LightningScytheLoot(this, 0.06D));
        addSubModule(new WindBladeLoot(this, 0.06D));
    }

    @Override
    public ChatColor getDisplayChatColor() {
        return ChatColor.GREEN;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                UtilString.pair("<gray>Left-Click", "<yellow>Preview"),
                UtilString.pair("<gray>Right-Click", "<yellow>Open")
        };
    }
}