
package jdz.pwarp.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.misc.StringUtils;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpManager;
import jdz.pwarp.events.WarpLoreEvent;

@CommandLabel("setLore")
@CommandRequiredArgs(3)
@CommandPermission("pwarp.setLore")
@CommandUsage("setLore <warp> <line> <lore>")
class SetWarpLore extends SubCommand {

	@Override
	public void execute(CommandSender sender, Set<String> flags, String... args) {
		int index;
		try {
			index = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED+"'"+args[1]+"' is not a valid number");
			return;
		}
		
		PlayerWarp warp = WarpManager.getInstance().get((Player)sender, args[0]);
		if (warp == null) {
			sender.sendMessage("No warp found called '"+args[0]+"'");
			return;
		}
		
		new WarpLoreEvent((Player)sender, warp, StringUtils.arrayToString(args, 2, " "), index).call();
	}

}
