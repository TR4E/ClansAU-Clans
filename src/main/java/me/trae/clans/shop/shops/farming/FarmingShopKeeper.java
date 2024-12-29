package me.trae.clans.shop.shops.farming;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.clans.shop.shops.farming.items.SugarCaneShopItem;
import me.trae.clans.shop.shops.farming.items.WheatShopItem;
import me.trae.clans.shop.shops.farming.items.blocks.CactusShopItem;
import me.trae.clans.shop.shops.farming.items.blocks.MelonBlockShopItem;
import me.trae.clans.shop.shops.farming.items.blocks.PumpkinBlockShopItem;
import me.trae.clans.shop.shops.farming.items.item_seeds.*;
import me.trae.clans.shop.shops.farming.items.seeds.MelonSeedsShopItem;
import me.trae.clans.shop.shops.farming.items.seeds.PumpkinSeedsShopItem;
import me.trae.clans.shop.shops.farming.items.seeds.WheatSeedsShopItem;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

import java.util.Arrays;
import java.util.List;

public class FarmingShopKeeper extends ShopKeeper {

    public FarmingShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        // Blocks
        addSubModule(new CactusShopItem(this));
        addSubModule(new MelonBlockShopItem(this));
        addSubModule(new PumpkinBlockShopItem(this));

        // Item Seeds
        addSubModule(new CarrotShopItem(this));
        addSubModule(new CocoaBeansShopItem(this));
        addSubModule(new MelonShopItem(this));
        addSubModule(new NetherStalkShopItem(this));
        addSubModule(new PotatoShopItem(this));

        // Seeds
        addSubModule(new MelonSeedsShopItem(this));
        addSubModule(new PumpkinSeedsShopItem(this));
        addSubModule(new WheatSeedsShopItem(this));

        // Other
        addSubModule(new SugarCaneShopItem(this));
        addSubModule(new WheatShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Farming";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.VILLAGER;
    }

    @Override
    public List<Location> getLocations() {
        return Arrays.asList(
                new Location(UtilWorld.getDefaultWorld(), -406.0D, 65.0D, 9.0D, UtilLocation.getYawByDirectionType(DirectionType.NORTH), 0.0F),
                new Location(UtilWorld.getDefaultWorld(), 406.0D, 65.0D, -9.0D, UtilLocation.getYawByDirectionType(DirectionType.SOUTH), 0.0F)
        );
    }

    @Override
    public void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
        UtilJava.cast(Villager.class, entity).setProfession(Villager.Profession.BUTCHER);
    }
}