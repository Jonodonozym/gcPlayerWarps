
package jdz.pwarp.data;

import java.util.List;

import org.bukkit.Location;

import jdz.bukkitUtils.persistence.minecraft.DBProvider;
import jdz.pwarp.PlayerWarpPlugin;

public interface WarpDatabase {
	public static final DBProvider<WarpDatabase> dbProvider = new DBProvider<>(
			PlayerWarpPlugin.getInstance(), () -> {
				return WarpDatabaseSQL.getInstance();
			}, () -> {
				return WarpDatabaseYML.getInstance();
			});

	public static WarpDatabase getInstance() {
		return dbProvider.get();
	}

	public void runOnConnect(Runnable r);

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
