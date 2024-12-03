package me.trae.clans.perk.commands;

import me.trae.champions.weapon.types.ChampionsPvPWeapon;
import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.enums.Rank;
import me.trae.core.command.types.Command;
import me.trae.core.command.types.models.PerkCommandType;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.gamer.Gamer;
import me.trae.core.perk.Perk;
import me.trae.core.perk.types.TitanRank;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import me.trae.core.weapon.Weapon;
import me.trae.core.weapon.WeaponManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class RepairCommand extends Command<Clans, PerkManager> implements PerkCommandType {

    @ConfigInject(type = Long.class, path = "Recharge", defaultValue = "14_400_000")
    private long recharge;

    public RepairCommand(final PerkManager manager) {
        super(manager, "repair", new String[0], Rank.ADMIN);
    }

    @Override
    public List<Class<? extends Perk<?, ?>>> getRequiredPerkClasses() {
        return Collections.singletonList(TitanRank.class);
    }

    @Override
    public void execute(final Player player, final Client client, final Gamer gamer, final String[] args) {
        final ItemStack itemStack = player.getEquipment().getItemInHand();
        if (itemStack == null || itemStack.getType().getMaxDurability() <= 0) {
            UtilMessage.message(player, "Repair", "You are not holding a valid Material!");
            return;
        }

        final Weapon<?, ?, ?> weapon = this.getInstance(Core.class).getManagerByClass(WeaponManager.class).getWeaponByItemStack(itemStack);
        if (weapon != null && !(weapon instanceof ChampionsPvPWeapon)) {
            UtilMessage.message(player, "Repair", "You cannot repair that item!");
            return;
        }

        if (itemStack.getDurability() == 0) {
            UtilMessage.simpleMessage(player, "Repair", "<yellow><var></yellow> is already repaired.", Collections.singletonList(UtilString.clean(itemStack.getType().name())));
            return;
        }

        if (!(this.getInstance(Core.class).getManagerByClass(RechargeManager.class).add(player, this.getName(), this.recharge, true))) {
            return;
        }

        itemStack.setDurability((short) 0);

        UtilMessage.simpleMessage(player, "Repair", "You repaired <yellow><var></yellow>.", Collections.singletonList(UtilString.clean(itemStack.getType().name())));
    }
}