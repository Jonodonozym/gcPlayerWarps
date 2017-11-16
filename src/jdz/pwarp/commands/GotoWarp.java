
package jdz.pwarp.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandRequiredArgs;
import jdz.bukkitUtils.commands.annotations.CommandUsage;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.PlayerWarp;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.events.WarpRequestEvent;
import net.md_5.bungee.api.ChatColor;

@CommandLabel("warp")
@CommandLabel("go")
@CommandLabel("w")
@CommandRequiredArgs(2)
@CommandUsage("/pwarp warp <player> <warpName>")
@CommandPermission("pwarp.teleport")
class GotoWarp extends SubCommand{

	@Override
	public boolean execute(CommandSender sender, String... args) {
		Player player = (Player) sender;
		
		@SuppressWarnings("deprecation")
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (target == null) {
			player.sendMessage(ChatColor.RED + args[0] + " has never logged in before!");
			return true;
		}

		if (args.length < 2)
			warpToFirst(player, target);
		else
			warp(player, target, args[1]);
		
		return true;
	}

	private void warpToFirst(Player player, OfflinePlayer target) {
		PlayerWarp warp = WarpDatabase.instance.getFirstWarp(target);
		if (warp == null)
			player.sendMessage(ChatColor.RED + target.getName() + " doesn't have any warps!");
		else
			tryWarp(player, warp);
	}

	private void warp(Player player, OfflinePlayer target, String warpName) {
		PlayerWarp warp = WarpDatabase.instance.getWarp(target, warpName);
		if (warp == null)
			player.sendMessage(ChatColor.RED + target.getName() + " doesn't have a warp called " + warpName);
		else
			tryWarp(player, warp);
	}
	
	private void tryWarp(Player player, PlayerWarp warp){
		if (!warp.isInClaimed()){
			removeInvalidWarp(warp);
			player.sendMessage(ChatColor.RED+"That warp is no longer in land accessable by the owner and has been removed");
		}
		else if (!warp.isSafe()){
			removeUnsafeWarp(warp);
			player.sendMessage(ChatColor.RED+"That warp is no longer safe and has been removed");
		}
		else new WarpRequestEvent(player, warp).call();
	}
	
	private void removeInvalidWarp(PlayerWarp warp){
		PlayerWarpPlugin.sqlMessageQueue.addQueuedMessage(warp.getOwner(), ChatColor.RED+"Your warp "+warp.getName()+" at"+warp.getLocation().toString()+" was removed since you no longer have access to it's location");
		WarpDatabase.instance.delWarp(warp);
	}
	
	private void removeUnsafeWarp(PlayerWarp warp){
		PlayerWarpPlugin.sqlMessageQueue.addQueuedMessage(warp.getOwner(), ChatColor.RED+"Your warp "+warp.getName()+" at"+warp.getLocation().toString()+" was removed since it is no longer in a safe location");
		WarpDatabase.instance.delWarp(warp);
	}
}
