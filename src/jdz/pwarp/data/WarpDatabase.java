
package jdz.pwarp.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import jdz.bukkitUtils.fileIO.FileLogger;
import jdz.bukkitUtils.misc.WorldUtils;
import jdz.bukkitUtils.sql.Database;
import jdz.bukkitUtils.sql.SqlColumn;
import jdz.bukkitUtils.sql.SqlColumnType;
import jdz.pwarp.PlayerWarpPlugin;
import lombok.Getter;

class WarpDatabase extends Database {
	@Getter
	private static final WarpDatabase instance = new WarpDatabase(PlayerWarpPlugin.instance);

	private final String table = "gcPlayerWarps";
	private final SqlColumn[] columns = new SqlColumn[] { new SqlColumn("player", SqlColumnType.STRING_64),
			new SqlColumn("warpName", SqlColumnType.STRING_64), new SqlColumn("location", SqlColumnType.STRING_256),
			new SqlColumn("lore1", SqlColumnType.STRING_256), new SqlColumn("lore2", SqlColumnType.STRING_256),
			new SqlColumn("lore3", SqlColumnType.STRING_256), new SqlColumn("daysPaid", SqlColumnType.INT_2_BYTE) };

	private WarpDatabase(JavaPlugin plugin) {
		super(plugin);

		api.runOnConnect(() -> {
			api.addTable(table, columns);
		});
	}

	public void addWarp(PlayerWarp warp) {
		String update = "INSERT INTO " + table + " (player, location, warpName, lore1, lore2, lore3, daysPaid) VALUES('"
				+ warp.getOwner().getName() + "','" + WorldUtils.locationToString(warp.getLocation()) + "','"
				+ warp.getName() + "','" + warp.getLore().get(0) + "','" + warp.getLore().get(1) + "','"
				+ warp.getLore().get(2) + "'," + WarpConfig.rentFreeDays + ");";
		api.executeUpdateAsync(update);
	}

	public void move(PlayerWarp warp, Location l) {
		String update = "UPDATE " + table + " SET location = '" + WorldUtils.locationToString(l) + "' WHERE player = '"
				+ warp.getOwner().getName() + "' AND warpName = '" + warp.getName() + "';";
		api.executeUpdateAsync(update);
	}

	public void delWarp(PlayerWarp warp) {
		String update = "DELETE FROM " + table + " WHERE player = '" + warp.getOwner().getName() + "' AND warpName = '"
				+ warp.getName() + "';";
		api.executeUpdateAsync(update);
	}

	private List<String> getLores(String[] result, int startIndex) {
		String lore1 = result[startIndex++];
		String lore2 = result[startIndex++];
		String lore3 = result[startIndex];
		int size = lore3.equals("") ? lore2.equals("") ? lore1.equals("") ? 0 : 1 : 2 : 3;
		List<String> lore = new ArrayList<String>(size);
		if (size > 2)
			lore.add(lore3);
		if (size > 1)
			lore.add(lore2);
		if (size > 0)
			lore.add(lore1);
		return lore;
	}

	public void rename(PlayerWarp warp, String newName) {
		String update = "UPDATE " + table + " SET warpName = '" + newName + "' WHERE player = '"
				+ warp.getOwner().getName() + "' AND warpName = '" + warp.getName() + "';";
		api.executeUpdateAsync(update);
	}

	public void setLore(PlayerWarp warp, String lore, int index) {
		if (index < 1 || index > 3) {
			new FileLogger(PlayerWarpPlugin.instance).createErrorLog(
					new RuntimeException("Invalid lore index " + index + ", should be between 1 and 3"));
			return;
		}

		String update = "UPDATE " + table + " SET lore" + index + " = '" + lore + "' WHERE player = '"
				+ warp.getOwner().getName() + "' AND warpName = '" + warp.getName() + "';";
		api.executeUpdateAsync(update);
	}

	public void setRentDays(PlayerWarp warp, int daysPaid) {
		if (daysPaid > WarpConfig.rentMaxDays)
			daysPaid = WarpConfig.rentMaxDays;

		String update = "UPDATE " + table + " SET daysPaid = " + daysPaid + " WHERE player = '"
				+ warp.getOwner().getName() + "' AND warpName = '" + warp.getName() + "';";
		api.executeUpdateAsync(update);
	}

	public void decreaseRentDays() {
		decreaseRentDays(1);
	}

	public void decreaseRentDays(int daysPaid) {
		String update = "UPDATE " + table + " SET daysPaid = daysPaid - " + daysPaid + ";";
		api.executeUpdateAsync(update);
	}

	@SuppressWarnings("deprecation")
	public List<PlayerWarp> getAllWarps() {
		String query = "SELECT location, warpName, lore1, lore2, lore3, daysPaid, player FROM " + table;
		List<String[]> result = api.getRows(query);

		List<PlayerWarp> warps = new ArrayList<PlayerWarp>();
		for (String[] s : result)
			warps.add(new PlayerWarp(Bukkit.getOfflinePlayer(s[6]), WorldUtils.locationFromString(s[0]), s[1],
					getLores(s, 2), Integer.parseInt(s[5])));

		return warps;
	}
}
