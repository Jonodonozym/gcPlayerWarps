
package jdz.pwarp.commands;

import java.util.ArrayList;
import java.util.List;

import jdz.bukkitUtils.commands.CommandExecutor;
import jdz.bukkitUtils.commands.ParentCommand;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandUsage;

@CommandLabel("rent")
@CommandUsage("rent <sub-command>")
@CommandPermission("pwarp.rent")
class Rent extends ParentCommand {
	private final RentList rentList = new RentList();
	private final List<SubCommand> commands = new ArrayList<>();

	public Rent(CommandExecutor executor) {
		super(executor);
		commands.add(rentList);
		commands.add(new RentPay());
		commands.add(new RentPayAll());
		setDefaultCommand(rentList);
	}

	@Override
	protected List<SubCommand> getSubCommands() {
		return commands;
	}


}
