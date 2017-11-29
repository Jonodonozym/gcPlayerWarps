
package jdz.pwarp.commands;

import java.util.Set;

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

@CommandLabel("setwarp")
@CommandRequiredArgs(2)
@CommandPermission("pwarp.admin")
@CommandUsage("/awarp setwarp <player> <warp>")
public class AdminSetWarp extends SubCommand{
	private final SetWarp piggyback = new SetWarp();

	@Override
	public void execute(CommandSender sender, Set<String> flags, String... args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (target == null)
			sender.sendMessage(ChatColor.RED+args[0]+" has never logged in before");
		else
			piggyback.setWarp((Player)sender, target, args[1]);
	}

}
