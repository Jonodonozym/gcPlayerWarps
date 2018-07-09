
package jdz.pwarp.commands;

import java.util.ArrayList;
import java.util.List;

import jdz.bukkitUtils.commands.CommandExecutor;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandExecutorPermission;
import jdz.bukkitUtils.commands.annotations.CommandExecutorPlayerOnly;
import jdz.pwarp.PlayerWarpPlugin;

@CommandExecutorPlayerOnly
@CommandExecutorPermission("pwarp.admin")
public class AdminCommandExecutor extends CommandExecutor {
	private List<SubCommand> commands = new ArrayList<SubCommand>();

	public AdminCommandExecutor() {
		super(PlayerWarpPlugin.getInstance(), "awarp", true);

		commands.add(new AdminSetWarp());
		commands.add(new AdminDelWarp());
		commands.add(new AdminListWarps());
		commands.add(new AdminReloadConfig());
	}

	@Override
	protected List<SubCommand> getSubCommands() {
		return commands;
	}
}
