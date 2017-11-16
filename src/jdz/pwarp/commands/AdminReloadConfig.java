
package jdz.pwarp.commands;

import org.bukkit.command.CommandSender;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.pwarp.data.WarpConfig;
import net.md_5.bungee.api.ChatColor;

@CommandLabel("reloadConfig")
@CommandLabel("reload")
@CommandLabel("r")
@CommandPermission("pwarp.op")
@CommandUsage("/awarp reloadConfig")
public class AdminReloadConfig extends SubCommand {

	@Override
	public boolean execute(CommandSender sender, String... args) {
		WarpConfig.reloadConfig();
		sender.sendMessage(ChatColor.GREEN+"Player warp config reloaded!");
		return true;
	}

}
