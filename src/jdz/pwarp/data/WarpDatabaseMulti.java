
package jdz.pwarp.data;

import java.util.List;

import org.bukkit.Location;

import lombok.Getter;

class WarpDatabaseMulti implements WarpDatabase{
	@Getter private static final WarpDatabaseMulti instance = new WarpDatabaseMulti();
	
	private WarpDatabaseMulti() { }
	
	@Override
	public void addWarp(PlayerWarp warp) {
		WarpDatabaseYML.getInstance().addWarp(warp);
		WarpDatabaseSQL.getInstance().addWarp(warp);
	}

	@Override
	public void move(PlayerWarp warp, Location l) {
		WarpDatabaseYML.getInstance().move(warp, l);
		WarpDatabaseSQL.getInstance().move(warp, l);
	}

	@Override
	public void delWarp(PlayerWarp warp) {
		WarpDatabaseYML.getInstance().delWarp(warp);
		WarpDatabaseSQL.getInstance().delWarp(warp);
	}

	@Override
	public void rename(PlayerWarp warp, String newName) {
		WarpDatabaseYML.getInstance().rename(warp, newName);
		WarpDatabaseSQL.getInstance().rename(warp, newName);
	}

	@Override
	public void setLore(PlayerWarp warp, String lore, int index) {
		WarpDatabaseYML.getInstance().setLore(warp, lore, index);
		WarpDatabaseSQL.getInstance().setLore(warp, lore, index);
	}

	@Override
	public void setRentDays(PlayerWarp warp, int daysPaid) {
		WarpDatabaseYML.getInstance().setRentDays(warp, daysPaid);
		WarpDatabaseSQL.getInstance().setRentDays(warp, daysPaid);
	}

	@Override
	public void decreaseRentDays() {
		WarpDatabaseYML.getInstance().decreaseRentDays();
		WarpDatabaseSQL.getInstance().decreaseRentDays();
	}

	@Override
	public void decreaseRentDays(int daysPaid) {
		WarpDatabaseYML.getInstance().decreaseRentDays(daysPaid);
		WarpDatabaseSQL.getInstance().decreaseRentDays(daysPaid);
	}

	@Override
	public List<PlayerWarp> getAllWarps() {
		return WarpDatabaseYML.getInstance().getAllWarps();
	}

}
