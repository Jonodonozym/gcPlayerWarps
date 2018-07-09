
package jdz.pwarp.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpManager;

@CommandLabel("list")
@CommandUsage("rent list")
public class RentList extends SubCommand {

	@Override
	public void execute(CommandSender sender, String... args) {
		listWarps((Player) sender, (Player) sender);
	}

	public void listWarps(CommandSender sender, OfflinePlayer target) {
		List<PlayerWarp> warps = WarpManager.getInstance().getAll(target);

		String[] list = new String[warps.size() + 2];
		list[0] = sender.equals(target)
				? ChatColor.GRAY + "============[ " + ChatColor.GOLD + "Warp Rent " + ChatColor.GRAY + "]============"
				: ChatColor.GRAY + "============[ " + ChatColor.GOLD + target.getName() + "'s Rent " + ChatColor.GRAY
						+ "]============";
		int i = 0;
		for (PlayerWarp warp : warps) {
			list[i + 1] = ChatColor.GREEN + "[" + warp.getName() + "] " + ChatColor.GOLD + " Days paid: "
					+ warp.getRentDaysPaid();
			i++;
		}

		list[list.length - 1] = ChatColor.GRAY + "===================================";
		sender.sendMessage(list);
	}
}
