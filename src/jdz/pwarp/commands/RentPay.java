
package jdz.pwarp.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.vault.VaultLoader;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpConfig;
import jdz.pwarp.data.WarpManager;

@CommandLabel("pay")
@CommandRequiredArgs(2)
@CommandUsage("rent pay <warp> <days>")
class RentPay extends SubCommand {

	@Override
	public void execute(CommandSender sender, Set<String> flags, String... args) {
		Player player = (Player) sender;

		PlayerWarp warp = WarpManager.getInstance().get(player, args[0]);

		if (warp == null) {
			sender.sendMessage(ChatColor.RED + "You don't have a warp called '" + args[0] + "'");
			return;
		}

		int numDaysPaid = warp.getRentDaysPaid();

		int daysToPay;
		try {
			daysToPay = Integer.parseInt(args[1]);
			if (daysToPay < 1)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a valid number of days");
			return;
		}

		if (numDaysPaid >= WarpConfig.rentMaxDays) {
			sender.sendMessage(ChatColor.RED + "You have already paid the maximum of " + WarpConfig.rentMaxDays
					+ " days in advance");
			return;
		}

		int totalDaysPaid = daysToPay + numDaysPaid;
		if (totalDaysPaid > WarpConfig.rentMaxDays)
			daysToPay = WarpConfig.rentMaxDays - numDaysPaid;

		double required = WarpConfig.rentCost * daysToPay;
		double balance = VaultLoader.getEconomy().getBalance(player);
		if (balance >= required) {
			VaultLoader.getEconomy().withdrawPlayer(player, required);
			warp.setRentDaysPaid(daysToPay + numDaysPaid);
			PlayerWarpPlugin.logger.log(player.getName() + " paid rent for " + daysToPay + " days for warp " + args[0]);
			player.sendMessage(ChatColor.GREEN + "You paid rent for " + daysToPay + ", costing "
					+ VaultLoader.getEconomy().format(required));
		} else
			player.sendMessage(ChatColor.RED + "You need " + VaultLoader.getEconomy().format(required - balance)
					+ " more to do that");
	}

}
