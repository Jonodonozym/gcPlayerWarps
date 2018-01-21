
package jdz.pwarp.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import jdz.bukkitUtils.vault.VaultLoader;
import jdz.pwarp.events.WarpRentExpiredEvent;
import lombok.Getter;

public class WarpManager {
	@Getter private static final WarpManager instance = new WarpManager();
	
	private final List<PlayerWarp> warps;
	
	private WarpManager() {
		warps = WarpDatabase.getInstance().getAllWarps();
	}
	
	public PlayerWarp get(OfflinePlayer owner) {
		for (PlayerWarp warp: warps)
			if (warp.getOwner().equals(owner))
				return warp;
		return null;
	}
	
	public PlayerWarp get(OfflinePlayer owner, String name) {
		for (PlayerWarp warp: warps)
			if (warp.getOwner().equals(owner) && warp.getName().equalsIgnoreCase(name))
				return warp;
		return null;
	}
	
	public List<PlayerWarp> getAll(OfflinePlayer owner) {
		List<PlayerWarp> warps = new ArrayList<PlayerWarp>();
		for (PlayerWarp warp: this.warps)
			if (warp.getOwner().equals(owner))
				warps.add(warp);
		return warps;
	}
	
	public void add(PlayerWarp warp) {
		warps.add(warp);
		WarpDatabase.getInstance().addWarp(warp);
	}
	
	public void remove(PlayerWarp warp) {
		warps.remove(warp);
		WarpDatabase.getInstance().delWarp(warp);
	}
	
	public List<PlayerWarp> getAllWarps() {
		return warps;
	}

	@SuppressWarnings("deprecation")
	public int getNumWarpsAllowed(OfflinePlayer player) {
		if (VaultLoader.getPermission().playerHas(Bukkit.getServer().getWorlds().get(0), player.getName(), "pwarp.setwarp.*"))
			return 30;
			
		int i = 30;
		while (true) {
			if (VaultLoader.getPermission().playerHas(Bukkit.getServer().getWorlds().get(0), player.getName(), "pwarp.setwarp."+i))
				return i;
			i--;
			if (i <= 0)
				return 0;
		}
	}

	public void decreaseRentDays() {
		List<PlayerWarp> noRentLeft = new ArrayList<PlayerWarp>();
		for (PlayerWarp warp: warps) {
			warp.setRentDaysPaid(warp.getRentDaysPaid()-1);
			if (warp.getRentDaysPaid() < 0)
				noRentLeft.add(warp);
		}
		for (PlayerWarp warp: noRentLeft)
			new WarpRentExpiredEvent(warp).call();
	}
}
