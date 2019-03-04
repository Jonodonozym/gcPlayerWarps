
package jdz.pwarp.commands;

import java.util.ArrayList;
import java.util.List;

import jdz.bukkitUtils.commands.AboutPluginCommand;
import jdz.bukkitUtils.commands.CommandExecutor;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandExecutorPlayerOnly;
import jdz.pwarp.PlayerWarpPlugin;

@CommandExecutorPlayerOnly
public class PlayerCommandExecutor extends CommandExecutor {
	private final List<SubCommand> commands = new ArrayList<>();

	public PlayerCommandExecutor() {
		super(PlayerWarpPlugin.getInstance(), "pwarp", true);

		commands.add(new GotoWarp());
		commands.add(new SetWarp());
		commands.add(new DeleteWarp());
		commands.add(new SetWarpLore());
		commands.add(new ListWarps());
		commands.add(new Rent(this));
		commands.add(new AboutPluginCommand(PlayerWarpPlugin.getInstance()));

		setDefaultCommand(new Default());
	}

	@Override
	protected List<SubCommand> getSubCommands() {
		return commands;
	}

}
