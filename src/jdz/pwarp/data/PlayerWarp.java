
package jdz.pwarp.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import jdz.bukkitUtils.components.messengers.OfflineMessenger;
import jdz.bukkitUtils.persistence.ORM.NoSave;
import jdz.bukkitUtils.persistence.ORM.SQLDataClass;
import jdz.bukkitUtils.pluginExtensions.vault.VaultLoader;
import jdz.bukkitUtils.utils.WorldUtils;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.config.SafeWarpConfig;
import jdz.pwarp.config.WarpConfig;
import jdz.pwarp.events.WarpRequestEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.md_5.bungee.api.ChatColor;

@EqualsAndHashCode(of = { "owner", "name" }, callSuper = false)
public class PlayerWarp extends SQLDataClass {
	@Getter private final OfflinePlayer owner;
	@Getter private Location location;
	@Getter private String name;
	@NoSave @Getter private List<String> lore;
	@Getter private int rentDaysPaid;

	public PlayerWarp(OfflinePlayer owner, Location location, String name, List<String> lore, int rentDaysPaid) {
		this.owner = owner;
		this.location = location;
		this.name = name.replaceAll(".|\"|'|:", "");
		this.lore = lore;
		this.rentDaysPaid = rentDaysPaid;
	}

	public void setLoreLine(String lore, int line) {
		this.lore.set(line, lore);
		WarpDatabase.getInstance().setLore(this, lore, line);
	}

	public void setLocation(Location l) {
		location = l;
		WarpDatabase.getInstance().move(this, l);
	}

	public void setName(String name) {
		this.name = name;
		WarpDatabase.getInstance().rename(this, name);
	}

	public void setRentDaysPaid(int daysPaid) {
		rentDaysPaid = daysPaid;
		WarpDatabase.getInstance().setRentDays(this, daysPaid);
	}

	public PlayerWarp(OfflinePlayer owner, Location location, String name) {
		this(owner, location, name, new ArrayList<>(Arrays.asList("", "", "")), 10);
	}

	@SuppressWarnings("deprecation")
	public boolean isSafe() {
		if (SafeWarpConfig.isEnabled())
			for (int i = 0; i < 3; i++)
				if (SafeWarpConfig.getUnsafeBlocks()
						.contains(location.clone().add(0, i, 0).getBlock().getType().getId()))
					return false;
		if (location.getBlockY() == 0)
			return false;
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean isInClaimed() {
		if (VaultLoader.getPermission().playerHas(Bukkit.getServer().getWorlds().get(0), owner.getName(),
				"pwarp.bypasshooks"))
			return true;

		if (WarpConfig.isASkyblockEnabled()) {
			Island island = ASkyBlockAPI.getInstance().getIslandAt(location.clone());
			if (island == null || !island.getMembers().contains(owner.getUniqueId()))
				return false;
		}

		if (WarpConfig.isPlotSquaredEnabled()) {
			Plot plot = new PlotAPI().getPlot(location.clone());
			if (plot == null || !plot.getOwners().contains(owner.getUniqueId())
					&& !plot.getMembers().contains(owner.getUniqueId()))
				return false;
		}

		if (WarpConfig.isGriefPreventionEnabled()) {
			Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location.clone(), true, null);
			if (claim != null) {
				ArrayList<String> permissionsList = new ArrayList<>();
				claim.getPermissions(permissionsList, new ArrayList<String>(), permissionsList, permissionsList);
				if (!permissionsList.contains(owner.getUniqueId().toString()))
					return false;
			}
			else
				return false;
		}

		return true;
	}

	public void warp(Player player) {
		moveToSolid();

		if (!isInClaimed()) {
			OfflineMessenger.getMessenger(PlayerWarpPlugin.getInstance()).message(getOwner(),
					ChatColor.RED + "Your warp '" + getName() + "' at " + WorldUtils.locationToLegibleString(location)
							+ " was removed since you no longer have access to it's location");
			WarpDatabase.getInstance().delWarp(this);
			if (getOwner().isOnline() && !getOwner().getPlayer().equals(player))
				player.sendMessage(
						ChatColor.RED + "That warp is no longer in land accessable by the owner and has been removed");
		}
		else if (!isSafe()) {
			OfflineMessenger.getMessenger(PlayerWarpPlugin.getInstance()).message(getOwner(),
					ChatColor.RED + "Your warp '" + getName() + "' at " + WorldUtils.locationToLegibleString(location)
							+ " was removed since it is no longer in a safe location");
			WarpDatabase.getInstance().delWarp(this);
			if (getOwner().isOnline() && !getOwner().getPlayer().equals(player))
				player.sendMessage(ChatColor.RED + "That warp is no longer safe and has been removed");
		}
		else
			new WarpRequestEvent(player, this).call();
	}

	private void moveToSolid() {
		Location startingLocation = location.clone();

		while (!location.getBlock().getType().isSolid()) {
			location.subtract(0, 1, 0);
			if (location.getBlockY() <= 0)
				break;
		}

		while (location.clone().add(0, 1, 0).getBlock().getType().isSolid()
				|| location.clone().add(0, 2, 0).getBlock().getType().isSolid())
			location.add(0, 1, 0);

		if (!location.equals(startingLocation))
			WarpDatabase.getInstance().move(this, location);
	}

	@Override
	public PlayerWarp clone() {
		PlayerWarp newWarp = new PlayerWarp(owner, location, name);
		newWarp.lore = new ArrayList<>(lore);
		newWarp.rentDaysPaid = rentDaysPaid;
		return newWarp;
	}
}
