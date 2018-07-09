
package jdz.pwarp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.misc.StringUtils;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpManager;

@CommandLabel("list")
@CommandLabel("warps")
@CommandUsage("list <player>")
@CommandPermission("pwarp.list")
@SuppressWarnings("deprecation")
class ListWarps extends SubCommand {

	@Override
	public void execute(CommandSender sender, String... args) {
		OfflinePlayer targetPlayer = (OfflinePlayer) sender;

		if (args.length > 0) {
			targetPlayer = Bukkit.getOfflinePlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage(ChatColor.RED + args[0] + " has never logged in before!");
				return;
			}
		}

		boolean same = targetPlayer.equals(sender);

		List<PlayerWarp> warps = WarpManager.getInstance().getAll(targetPlayer);
		List<String> warpNames = new ArrayList<String>();
		for (PlayerWarp warp : warps)
			warpNames.add(warp.getName());

		if (warpNames.isEmpty())
			sender.sendMessage(ChatColor.GRAY + (same ? "You have no warps" : args[0] + " has no warps"));
		else {
			sender.sendMessage(ChatColor.GRAY + "============[ " + ChatColor.GOLD + (same ? "My" : args[0] + "'s")
					+ " Warps " + ChatColor.GRAY + "]============");
			sender.sendMessage(StringUtils.listToString(warpNames, ","));
			sender.sendMessage(ChatColor.GRAY + "==================================");
		}
	}

}
