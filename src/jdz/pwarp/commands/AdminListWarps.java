
package jdz.pwarp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;

@CommandLabel("list")
@CommandRequiredArgs(1)
@CommandPermission("pwarp.admin")
@CommandUsage("list <player>")
public class AdminListWarps extends SubCommand {
	private final RentList piggyback = new RentList();

	@Override
	public void execute(CommandSender sender, String... args) {
		@SuppressWarnings("deprecation") OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (target == null)
			sender.sendMessage(ChatColor.RED + args[0] + " has never logged in before");
		else
			piggyback.listWarps(sender, target);
	}

}
