package me.trae.clans.item.modules;

import me.trae.clans.Clans;
import me.trae.clans.item.ItemManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.ItemBuilder;
import me.trae.core.item.events.ItemUpdateEvent;
import me.trae.core.utility.UtilString;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class ChirpDisc extends SpigotListener<Clans, ItemManager> {

    public ChirpDisc(final ItemManager manager) {
        super(manager);
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        final ItemBuilder builder = event.getBuilder();

        if (builder.getItemStack().getType() != Material.RECORD_4) {
            return;
        }

        builder.setDisplayName("<red>AusMC Classical Record");
        builder.setLore(new String[]{
                "The nostalgic record held between 2011-2012.",
                "",
                UtilString.pair("<gray>Song", "<gold>Chirp"),
                UtilString.pair("<gray>Author", "<yellow>Chiss")
        });
    }
}