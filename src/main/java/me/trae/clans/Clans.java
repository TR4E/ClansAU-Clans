package me.trae.clans;

import me.trae.clans.clan.ClanManager;
import me.trae.clans.config.ConfigManager;
import me.trae.clans.crate.CrateManager;
import me.trae.clans.donation.DonationManager;
import me.trae.clans.farming.FarmingManager;
import me.trae.clans.fields.FieldsManager;
import me.trae.clans.fishing.FishingManager;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.item.ItemManager;
import me.trae.clans.koth.KothManager;
import me.trae.clans.perk.PerkManager;
import me.trae.clans.preference.PreferenceManager;
import me.trae.clans.quest.QuestManager;
import me.trae.clans.recipe.RecipeManager;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.tip.TipManager;
import me.trae.clans.tnt.TntManager;
import me.trae.clans.weapon.WeaponManager;
import me.trae.clans.world.WorldManager;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.framework.types.plugin.MiniPlugin;

public class Clans extends MiniPlugin {

    @Override
    public void registerManagers() {
        addManager(new ClanManager(this));
        addManager(new ConfigManager(this));
        addManager(new DonationManager(this));
        addManager(new FarmingManager(this));
        addManager(new FieldsManager(this));
        addManager(new FishingManager(this));
        addManager(new GamerManager(this));
        addManager(new ItemManager(this));
        addManager(new WeaponManager(this));
        addManager(new KothManager(this));
        addManager(new PerkManager(this));
        addManager(new CrateManager(this));
        addManager(new PreferenceManager(this));
        addManager(new QuestManager(this));
        addManager(new RecipeManager(this));
        addManager(new ShopManager(this));
        addManager(new TipManager(this));
        addManager(new TntManager(this));
        addManager(new WorldManager(this));
        addManager(new WorldEventManager(this));
    }
}