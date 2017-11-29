package jdz.pwarp.commands;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.events.WarpDeletedEvent;
import net.md_5.bungee.api.ChatColor;

@CommandLabel("delwarp")
@CommandLabel("delete")
@CommandLabel("remove")
@CommandRequiredArgs(1)
@CommandUsage("/pwarp delwarp <name>")
@CommandPermission("pwarp.delwarp")
class DeleteWarp extends SubCommand{

	@Override
	public void execute(CommandSender sender, Set<String> flags, String... args) {
		delWarp(sender, (Player)sender, args[0]);
	}

	public void delWarp(CommandSender sender, OfflinePlayer target, String name) {
		PlayerWarp warp = new PlayerWarp(target, new Location(Bukkit.getWorlds().get(0),0,0,0), name);
		if (WarpDatabase.instance.exists(warp))
			new WarpDeletedEvent((Player)sender, warp).call();
		else
			sender.sendMessage(ChatColor.RED+"No warp found named "+name);
	}
}
