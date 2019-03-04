
package jdz.pwarp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;

@CommandLabel("setwarp")
@CommandRequiredArgs(2)
@CommandPermission("pwarp.admin")
@CommandUsage("setwarp <player> <warp>")
public class AdminSetWarp extends SubCommand {
	private final SetWarp piggyback = new SetWarp();

	@Override
	public void execute(CommandSender sender, String... args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (target == null)
			sender.sendMessage(ChatColor.RED + args[0] + " has never logged in before");
		else
			piggyback.setWarp((Player) sender, target, args[1]);
	}

}
