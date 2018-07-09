
package jdz.pwarp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.vault.VaultLoader;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpConfig;
import jdz.pwarp.data.WarpManager;

@CommandLabel("payall")
@CommandRequiredArgs(1)
@CommandUsage("rent payall <days>")
class RentPayAll extends SubCommand {

	@Override
	public void execute(CommandSender sender, String... args) {
		Player player = (Player) sender;
		List<PlayerWarp> warps = WarpManager.getInstance().getAll(player);

		int daysToPay;
		try {
			daysToPay = Integer.parseInt(args[0]);
			if (daysToPay < 1)
				throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is not a valid number of days");
			return;
		}

		double totalCost = 0;
		List<Integer> newDaysPaid = new ArrayList<Integer>();

		for (PlayerWarp warp : warps) {
			int thisWarpDays = daysToPay;
			int totalDaysPaid = daysToPay + warp.getRentDaysPaid();
			if (totalDaysPaid > WarpConfig.rentMaxDays)
				thisWarpDays = WarpConfig.rentMaxDays - warp.getRentDaysPaid();

			totalCost += WarpConfig.rentCost * warp.getRentDaysPaid();
			newDaysPaid.add(thisWarpDays + warp.getRentDaysPaid());
		}

		double balance = VaultLoader.getEconomy().getBalance(player);

		if (balance >= totalCost) {
			VaultLoader.getEconomy().withdrawPlayer(player, totalCost);

			int i = 0;
			for (PlayerWarp warp : warps)
				warp.setRentDaysPaid(newDaysPaid.get(i++));

			PlayerWarpPlugin.logger.log(player.getName() + " paid rent for " + daysToPay + " days for all warps");

			player.sendMessage(ChatColor.GREEN + "You paid rent for up to " + daysToPay + "on all warps, costing "
					+ VaultLoader.getEconomy().format(totalCost));
		}
		else
			player.sendMessage(ChatColor.RED + "You need " + VaultLoader.getEconomy().format(totalCost - balance)
					+ " more to do that");
	}

}
