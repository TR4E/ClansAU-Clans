package me.trae.clans.crate.menu;

import me.trae.clans.Clans;
import me.trae.clans.crate.Crate;
import me.trae.clans.crate.CrateManager;
import me.trae.clans.crate.loot.Loot;
import me.trae.clans.crate.menu.interfaces.ICrateMenu;
import me.trae.core.menu.Button;
import me.trae.core.menu.Menu;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CrateOpenMenu extends Menu<Clans, CrateManager> implements ICrateMenu {

    public CrateOpenMenu(final CrateManager manager, final Player player, final Crate crate) {
        super(manager, player, InventoryType.DISPENSER, UtilColor.bold(ChatColor.WHITE) + String.format("Opening %s", crate.getDisplayName()));
    }

    @Override
    public boolean fillBlank() {
        return true;
    }

    @Override
    public void fillPage(final Player player) {
        final Crate crate = this.getCrate();

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                new SoundCreator(Sound.ORB_PICKUP).play(player);

                final Loot randomLoot = crate.getRandomLoot();
                if (randomLoot == null) {
                    return;
                }

                addButton(randomLoot);

                construct();

                counter++;

                if (counter > 10) {
                    cancel();

                    new SoundCreator(Sound.LEVEL_UP, 2.0F, 2.0F).play(player);

                    randomLoot.reward(player);

                    closeMenuLater(player);
                }
            }
        }.runTaskTimer(this.getInstance(), 0L, 10L);
    }

    private void addButton(final Loot loot) {
        this.addButton(new Button<CrateOpenMenu>(this, 4, loot.getItemBuilder().toItemStack()) {
            @Override
            public void onClick(final Player player, final ClickType clickType) {
            }
        });
    }

    private void closeMenuLater(final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(CrateOpenMenu.this.getInventory().getViewers().contains(player))) {
                    return;
                }

                player.closeInventory();
            }
        }.runTaskLater(this.getInstance(), 3000L / 50L);
    }
}