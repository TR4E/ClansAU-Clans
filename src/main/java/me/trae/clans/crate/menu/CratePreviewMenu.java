package me.trae.clans.crate.menu;

import me.trae.clans.Clans;
import me.trae.clans.crate.Crate;
import me.trae.clans.crate.CrateManager;
import me.trae.clans.crate.loot.Loot;
import me.trae.clans.crate.menu.interfaces.ICrateMenu;
import me.trae.core.menu.Button;
import me.trae.core.menu.Menu;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public abstract class CratePreviewMenu extends Menu<Clans, CrateManager> implements ICrateMenu {

    public CratePreviewMenu(final CrateManager manager, final Player player, final Crate crate) {
        super(manager, player, 27, UtilColor.bold(ChatColor.WHITE) + UtilString.format("Previewing %s", crate.getDisplayName()));
    }

    @Override
    public void fillPage(final Player player) {
        int slot = 0;

        for (final Loot loot : this.getCrate().getSubModulesByClass(Loot.class)) {
            addButton(new Button<CratePreviewMenu>(this, slot, loot.getItemBuilder().toItemStack()) {
                @Override
                public void onClick(final Player player, final ClickType clickType) {
                }
            });

            slot++;
        }
    }
}