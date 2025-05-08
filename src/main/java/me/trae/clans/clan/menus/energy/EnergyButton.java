package me.trae.clans.clan.menus.energy;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.menus.energy.interfaces.IEnergyButton;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.clans.utility.UtilClans;
import me.trae.core.menu.Button;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilString;
import me.trae.core.utility.UtilTime;
import me.trae.core.utility.enums.TimeUnit;
import me.trae.core.utility.objects.SoundCreator;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public abstract class EnergyButton extends Button<EnergyMenu> implements IEnergyButton {

    public EnergyButton(final EnergyMenu menu, final int slot, final ItemStack itemStack) {
        super(menu, slot, itemStack);
    }

    @Override
    public String getDisplayName() {
        return UtilString.format("<green>Buy %s of Energy", this.getDurationText());
    }

    @Override
    public String[] getLore() {
        return new String[]{
                UtilString.pair("<gray>Cost", UtilString.format("<gold>%s", UtilString.toDollar(this.getCost())))
        };
    }

    @Override
    public void onClick(final Player player, final ClickType clickType) {
        if (!(Arrays.asList(ClickType.LEFT, ClickType.SHIFT_LEFT).contains(clickType))) {
            return;
        }

        final GamerManager gamerManager = this.getMenu().getInstance().getManagerByClass(GamerManager.class);

        final Gamer gamer = gamerManager.getGamerByPlayer(player);

        if (!(gamer.hasCoins(this.getCost()))) {
            new SoundCreator(Sound.ITEM_BREAK, 1.0F, 0.6F).play(player);
            return;
        }

        gamer.setCoins(gamer.getCoins() - this.getCost());
        gamerManager.getRepository().updateData(gamer, GamerProperty.COINS);

        final Clan clan = this.getMenu().getClan();

        final long duration = this.getHourMultiplier() * TimeUnit.HOURS.getDuration();

        clan.setEnergy(clan.getEnergy() + UtilClans.formatClanEnergy(clan, duration));
        this.getMenu().getManager().getRepository().updateData(clan, ClanProperty.ENERGY);

        clan.getOnlineMembers().keySet().forEach(memberPlayer -> UtilServer.callEvent(new ScoreboardUpdateEvent(memberPlayer)));

        new SoundCreator(Sound.NOTE_PLING, 1.0F, 2.0F).play(player);

        UtilMessage.simpleMessage(player, "Clans", "You have purchased <green><var></green> of Clan Energy.", Collections.singletonList(UtilTime.getTime(duration)));

        this.getMenu().getManager().messageClan(clan, "Clans", "<var> has purchased <green><var></green> of Clan Energy.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), UtilTime.getTime(duration)), Collections.singletonList(player.getUniqueId()));
    }

    @Override
    public int getCost() {
        final Clan clan = this.getMenu().getClan();

        final double depletionPerMinute = clan.getEnergyDepletionRatio() / TimeUnit.MINUTES.getDuration();

        final int durationMinutes = this.getHourMultiplier() * 60;

        final double costPerEnergy = this.getMenu().getManager().costPerEnergy;

        return (int) Math.round(depletionPerMinute * durationMinutes * costPerEnergy);
    }
}