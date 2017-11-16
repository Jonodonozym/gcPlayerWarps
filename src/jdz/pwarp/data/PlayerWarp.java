
package jdz.pwarp.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import jdz.bukkitUtils.vault.VaultLoader;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.events.WarpDeletedEvent;
import jdz.pwarp.events.WarpRequestEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.md_5.bungee.api.ChatColor;

public class PlayerWarp {
	private final OfflinePlayer owner;
	private Location location;
	private final String name;
	private final List<String> lore;
	private final int rentDaysPaid;
	
	public PlayerWarp(OfflinePlayer owner, Location location, String name, List<String> lore, int rentDaysPaid) {
		this.owner = owner;
		this.location = location;
		this.name = name;
		this.lore = lore;
		this.rentDaysPaid = rentDaysPaid;
	}

	public PlayerWarp(OfflinePlayer owner, Location location, String name) {
		this(owner, location, name, new ArrayList<String>(), 10);
	}

	@SuppressWarnings("deprecation")
	public boolean isSafe() {
		if (WarpConfig.safeWarpEnabled) {
			for (int i=0; i<3; i++)
				if (WarpConfig.unsafeBlocks.contains(location.add(0, i, 0).getBlock().getTypeId()))
					return false;
		}
		if (location.getBlockY() == 0)
			return false;
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean isInClaimed() {
		if (VaultLoader.getPermission().playerHas(Bukkit.getServer().getWorlds().get(0), owner.getName(),
				"pwarp.bypasshooks"))
			return true;

		if (WarpConfig.ASEnabled) {
			Island island = ASkyBlockAPI.getInstance().getIslandAt(location);
			if (island == null || (!island.getMembers().contains(owner.getUniqueId()))) {
				return false;
			}
		}

		if (WarpConfig.PSEnabled) {
			Plot plot = new PlotAPI().getPlot(location);
			if (plot == null || (!plot.getOwners().contains(owner.getUniqueId())
					&& !plot.getMembers().contains(owner.getUniqueId())))
				return false;
		}

		if (WarpConfig.GPEnabled) {
			Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
			if (claim != null) {
				ArrayList<String> permissionsList = new ArrayList<String>();
				claim.getPermissions(permissionsList, new ArrayList<String>(), permissionsList, permissionsList);
				if (!permissionsList.contains(owner.getUniqueId().toString()))
					return false;
			} else
				return false;
		}

		return true;
	}

	public void delete(CommandSender executor) {
		if (WarpDatabase.instance.exists(this))
			new WarpDeletedEvent((Player)executor, this).call();
		else
			executor.sendMessage(ChatColor.RED+"No warp found named "+name);
	}
	
	public void warp(Player player){
		moveToSolid();
		
		if (!isInClaimed()){
			PlayerWarpPlugin.sqlMessageQueue.addQueuedMessage(getOwner(), ChatColor.RED+"Your warp "+getName()+" at"+getLocation().toString()+" was removed since you no longer have access to it's location");
			WarpDatabase.instance.delWarp(this);
			if (getOwner().isOnline() && !getOwner().getPlayer().equals(player))
			player.sendMessage(ChatColor.RED+"That warp is no longer in land accessable by the owner and has been removed");
		}
		else if (!isSafe()){
			PlayerWarpPlugin.sqlMessageQueue.addQueuedMessage(getOwner(), ChatColor.RED+"Your warp "+getName()+" at"+getLocation().toString()+" was removed since it is no longer in a safe location");
			WarpDatabase.instance.delWarp(this);
			if (getOwner().isOnline() && !getOwner().getPlayer().equals(player))
			player.sendMessage(ChatColor.RED+"That warp is no longer safe and has been removed");
		}
		else new WarpRequestEvent(player, this).call();
	}
	
	private void moveToSolid() {
		Location startingLocation = location;
		while(!location.getBlock().getType().isSolid()) {
			location = location.subtract(0, 1, 0);
			if (location.getBlockY() <= 0)
				break;
		}
		while(location.add(0,2,0).getBlock().getType().isSolid())
			location = location.add(0,1,0);
		
		if (!location.equals(startingLocation))
			WarpDatabase.instance.setWarp(this);
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}
	
	public int getRentDaysPaid() {
		return rentDaysPaid;
	}
}
