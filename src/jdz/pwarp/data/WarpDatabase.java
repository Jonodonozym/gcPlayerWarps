
package jdz.pwarp.data;

import java.util.List;

import org.bukkit.Location;

public interface WarpDatabase {
	public static WarpDatabase getInstance() {
		return WarpDatabaseMulti.getInstance();
	}

	public static void runOnConnect(Runnable r) {
		if (getInstance() instanceof WarpDatabaseMulti || getInstance() instanceof WarpDatabaseSQL)
			WarpDatabaseSQL.getInstance().runOnConnect(r);
		else
			r.run();
	}

	public void addWarp(PlayerWarp warp);

	public void move(PlayerWarp warp, Location l);

	public void delWarp(PlayerWarp warp);

	public void rename(PlayerWarp warp, String newName);

	public void setLore(PlayerWarp warp, String lore, int index);

	public void setRentDays(PlayerWarp warp, int daysPaid);

	public void decreaseRentDays();

	public void decreaseRentDays(int daysPaid);

	public List<PlayerWarp> getAllWarps();
}
