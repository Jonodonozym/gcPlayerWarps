package jdz.pwarp.commands;


import java.util.Set;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.annotations.CommandShortDescription;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.misc.WorldUtils;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpConfig;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.events.WarpCreatedEvent;
import jdz.pwarp.events.WarpEvent;
import jdz.pwarp.events.WarpMovedEvent;
import net.md_5.bungee.api.ChatColor;

@CommandLabel("setwarp")
@CommandLabel("create")
@CommandRequiredArgs(1)
@CommandUsage("/pwarp setwarp <name>")
@CommandShortDescription("Creates or moves an existing warp to your location")
@CommandPermission("pwarp.setwarp")
final class SetWarp extends SubCommand{

	@Override
	public void execute(CommandSender sender, Set<String> flags, String... args) {
		setWarp((Player)sender, (Player)sender, args[0]);
	}

	public void setWarp(Player sender, OfflinePlayer target, String name) {
		int currentWarps = WarpDatabase.instance.getNumWarps(target);
		int allowedWarps = WarpDatabase.instance.getNumWarpsAllowed(target);

		if (currentWarps >= allowedWarps) {
			sender.sendMessage(ChatColor.RED + "You can only have " + allowedWarps + " warps at a time!");
			return;
		}

		Location location = WorldUtils.getNearestLocationUnder(sender.getLocation());
		if (location == null){
			sender.sendMessage(ChatColor.RED+"You are not standing above a block!");
			return;
		}
		
		PlayerWarp warp = new PlayerWarp(target, location, name);
		if (!warp.isInClaimed()){
			String part = target.isOnline() && sender.equals(target.getPlayer())? "your":"their";
			if (WarpConfig.ASEnabled)
				sender.sendMessage(ChatColor.RED+"You can only place warps on "+part+" island!");
			if (WarpConfig.PSEnabled)
				sender.sendMessage(ChatColor.RED+"You can only place warps in "+part+" plots");
			if (WarpConfig.GPEnabled)
				sender.sendMessage(ChatColor.RED+"You can only place warps in "+part+" claimed land");
			return;
		}
		
		if (!warp.isSafe()){
			sender.sendMessage(ChatColor.RED+"You cannot place warps on that type of block; it is unsafe!"); 
			return;
		}
		
		PlayerWarp existingWarp = WarpDatabase.instance.getWarp(target, name);
		
		WarpEvent event;
		if (existingWarp != null)
			event = new WarpMovedEvent(sender, existingWarp, location);
		else
			event = new WarpCreatedEvent(sender, warp);

		event.call();
	}
}
