package me.trae.clans.fields.modules.loot;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.fields.FieldsManager;
import me.trae.clans.fields.events.FieldsLootEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.ItemBuilder;
import me.trae.core.item.constants.ItemConstants;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMath;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilTitle;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HandleFieldsClanEnergyLoot extends SpigotListener<Clans, FieldsManager> {

    @ConfigInject(type = Integer.class, path = "Min-Energy", defaultValue = "25")
    private int minEnergy;

    @ConfigInject(type = Integer.class, path = "Max-Energy", defaultValue = "50")
    private int maxEnergy;

    public HandleFieldsClanEnergyLoot(final FieldsManager manager) {
        super(manager);
    }

    private ItemStack getItemStack() {
        return ItemConstants.LAPIS_LAZULI;
    }

    @EventHandler
    public void onFieldsLoot(final FieldsLootEvent event) {
        final Material material = event.getBlockMaterial();

        if (material != Material.LAPIS_ORE) {
            return;
        }

        String displayName = null;
        if (this.getInstance().getManagerByClass(ClanManager.class).energyEnabled) {
            displayName = "Clan Energy";
        }

        for (int i = 0; i < event.getMultiplier(); i++) {
            event.addLoot(new ItemBuilder(this.getItemStack(), displayName).toItemStack());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Item item = event.getItem();

        final ItemStack itemStack = item.getItemStack();
        if (itemStack == null) {
            return;
        }

        if (!(UtilItem.isSimilarWithoutItemMeta(this.getItemStack(), itemStack))) {
            return;
        }

        if (!(itemStack.hasItemMeta())) {
            return;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (!(itemMeta.hasDisplayName())) {
            return;
        }

        if (!(itemMeta.getDisplayName().contains("Clan Energy"))) {
            return;
        }

        event.setCancelled(true);

        item.remove();

        final Player player = event.getPlayer();

        int range = this.maxEnergy - this.minEnergy;
        range = minEnergy + UtilMath.getRandomNumber(Integer.class, 0, range / 3);

        range *= itemStack.getAmount();

        final ClanManager clanManager = this.getInstance().getManagerByClass(ClanManager.class);

        final Clan playerClan = clanManager.getClanByPlayer(player);
        if (playerClan == null) {
            return;
        }

        playerClan.setEnergy(playerClan.getEnergy() + range);
        clanManager.getRepository().updateData(playerClan, ClanProperty.ENERGY);

        new SoundCreator(Sound.LEVEL_UP, 2.0F, 2.0F).play(player);

        UtilServer.callEvent(new ScoreboardUpdateEvent(player));

        UtilTitle.sendActionBar(player, String.format("<light_purple>+%s Clan Energy", range));
    }
}