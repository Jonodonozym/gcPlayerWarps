
package jdz.pwarp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.events.WarpRenamedEvent;

@CommandLabel("rename")
@CommandRequiredArgs(2)
@CommandUsage("/pwarp rename <oldName> <newName>")
@CommandPermission("pwarp.setwarp")
class RenameWarp extends SubCommand {

	@Override
	public boolean execute(CommandSender sender, String... args) {
		PlayerWarp warp = WarpDatabase.instance.getWarp((Player) sender, args[0]);
		if (warp == null)
			sender.sendMessage("No warp found with the name '" + args[0] + ";");
		else
			new WarpRenamedEvent(sender, warp, args[1]).call();
		return true;
	}

}
