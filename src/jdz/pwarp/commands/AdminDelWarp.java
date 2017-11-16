
package jdz.pwarp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.commands.SubCommand;

@CommandLabel("delwarp")
@CommandRequiredArgs(2)
@CommandPermission("pwarp.admin")
@CommandUsage("/awarp delwarp <player> <warp>")
public class AdminDelWarp extends SubCommand{
	private final DeleteWarp piggyback = new DeleteWarp();

	@Override
	public boolean execute(CommandSender sender, String... args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (target == null)
			sender.sendMessage(ChatColor.RED+args[0]+" has never logged in before");
		else
			piggyback.delWarp((Player)sender, target, args[1]);
		return true;
	}

}
