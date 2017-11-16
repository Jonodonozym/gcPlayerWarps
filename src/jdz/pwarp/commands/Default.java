
package jdz.pwarp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.pwarp.gui.PlayerWarpGuiMenu;

@CommandLabel("DEFAULT")
class Default extends SubCommand {
	private final GotoWarp warpCommand = new GotoWarp();

	@Override
	public boolean execute(CommandSender sender, String... args) {
		if (args.length == 0)
			PlayerWarpGuiMenu.instance.open((Player)sender);
		else warpCommand.execute(sender, args);
		
		return true;
	}

}
