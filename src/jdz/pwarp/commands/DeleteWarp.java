package jdz.pwarp.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpManager;
import jdz.pwarp.events.WarpDeletedEvent;
import net.md_5.bungee.api.ChatColor;

@CommandLabel("delwarp")
@CommandLabel("delete")
@CommandLabel("remove")
@CommandRequiredArgs(1)
@CommandUsage("delwarp <name>")
@CommandPermission("pwarp.delwarp")
class DeleteWarp extends SubCommand {

	@Override
	public void execute(CommandSender sender, String... args) {
		delWarp(sender, (Player) sender, args[0]);
	}

	public void delWarp(CommandSender sender, OfflinePlayer target, String name) {
		PlayerWarp warp = WarpManager.getInstance().get(target, name);
		if (warp == null)
			sender.sendMessage(ChatColor.RED + "No warp found named " + name);
		else
			new WarpDeletedEvent((Player) sender, warp).call();
	}
}
