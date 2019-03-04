
package jdz.pwarp.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpManager;
import net.md_5.bungee.api.ChatColor;

@CommandLabel("warp")
@CommandLabel("go")
@CommandLabel("w")
@CommandRequiredArgs(2)
@CommandUsage("warp <player> <warpName>")
@CommandPermission("pwarp.teleport")
class GotoWarp extends SubCommand {

	@Override
	public void execute(CommandSender sender, String... args) {
		Player player = (Player) sender;

		@SuppressWarnings("deprecation")
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (target == null)
			player.sendMessage(ChatColor.RED + args[0] + " has never logged in before!");
		else if (args.length < 2)
			warpToFirst(player, target);
		else
			warp(player, target, args[1]);
	}

	private void warpToFirst(Player player, OfflinePlayer target) {
		PlayerWarp warp = WarpManager.getInstance().get(target);
		if (warp == null)
			player.sendMessage(ChatColor.RED + target.getName() + " doesn't have any warps!");
		else
			warp.warp(player);
	}

	private void warp(Player player, OfflinePlayer target, String warpName) {
		PlayerWarp warp = WarpManager.getInstance().get(target, warpName);
		if (warp == null)
			player.sendMessage(ChatColor.RED + target.getName() + " doesn't have a warp called " + warpName);
		else
			warp.warp(player);
	}
}
