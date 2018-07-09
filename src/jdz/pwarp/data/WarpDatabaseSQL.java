
package jdz.pwarp.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import jdz.bukkitUtils.fileIO.FileLogger;
import jdz.bukkitUtils.misc.WorldUtils;
import jdz.bukkitUtils.sql.SQLColumn;
import jdz.bukkitUtils.sql.SQLColumnType;
import jdz.bukkitUtils.sql.SQLRow;
import jdz.bukkitUtils.sql.SqlDatabase;
import jdz.pwarp.PlayerWarpPlugin;
import lombok.Getter;

public class WarpDatabaseSQL extends SqlDatabase implements WarpDatabase {
	@Getter private static final WarpDatabaseSQL instance = new WarpDatabaseSQL(PlayerWarpPlugin.getInstance());

	private final String table = "gcPlayerWarps";
	private final SQLColumn[] columns = new SQLColumn[] { new SQLColumn("player", SQLColumnType.STRING_64),
			new SQLColumn("warpName", SQLColumnType.STRING_64), new SQLColumn("location", SQLColumnType.STRING_256),
			new SQLColumn("lore1", SQLColumnType.STRING_256), new SQLColumn("lore2", SQLColumnType.STRING_256),
			new SQLColumn("lore3", SQLColumnType.STRING_256), new SQLColumn("daysPaid", SQLColumnType.INT_2_BYTE) };

	private WarpDatabaseSQL(JavaPlugin plugin) {
		super(plugin);

		runOnConnect(() -> {
			addTable(table, columns);
		});
	}

	@Override
	public void runOnConnect(Runnable r) {
		super.runOnConnect(r);
	}

	@Override
	public void addWarp(PlayerWarp warp) {
		String update = "INSERT INTO " + table + " (player, location, warpName, lore1, lore2, lore3, daysPaid) VALUES('"
				+ warp.getOwner().getName() + "','" + WorldUtils.locationToString(warp.getLocation()) + "','"
				+ warp.getName() + "','" + warp.getLore().get(0) + "','" + warp.getLore().get(1) + "','"
				+ warp.getLore().get(2) + "'," + WarpConfig.rentFreeDays + ");";
		updateAsync(update);
	}

	@Override
	public void move(PlayerWarp warp, Location l) {
		String update = "UPDATE " + table + " SET location = '" + WorldUtils.locationToString(l) + "' WHERE player = '"
				+ warp.getOwner().getName() + "' AND warpName = '" + warp.getName() + "';";
		updateAsync(update);
	}

	@Override
	public void delWarp(PlayerWarp warp) {
		String update = "DELETE FROM " + table + " WHERE player = '" + warp.getOwner().getName() + "' AND warpName = '"
				+ warp.getName() + "';";
		updateAsync(update);
	}

	private List<String> getLores(SQLRow row, int startIndex) {
		String lore1 = row.get(startIndex++);
		String lore2 = row.get(startIndex++);
		String lore3 = row.get(startIndex++);
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

	@Override
	public void rename(PlayerWarp warp, String newName) {
		String update = "UPDATE " + table + " SET warpName = '" + newName + "' WHERE player = '"
				+ warp.getOwner().getName() + "' AND warpName = '" + warp.getName() + "';";
		updateAsync(update);
	}

	@Override
	public void setLore(PlayerWarp warp, String lore, int index) {
		if (index < 1 || index > 3) {
			new FileLogger(PlayerWarpPlugin.getInstance()).createErrorLog(
					new RuntimeException("Invalid lore index " + index + ", should be between 1 and 3"));
			return;
		}

		String update = "UPDATE " + table + " SET lore" + index + " = '" + lore + "' WHERE player = '"
				+ warp.getOwner().getName() + "' AND warpName = '" + warp.getName() + "';";
		updateAsync(update);
	}

	@Override
	public void setRentDays(PlayerWarp warp, int daysPaid) {
		if (daysPaid > WarpConfig.rentMaxDays)
			daysPaid = WarpConfig.rentMaxDays;

		String update = "UPDATE " + table + " SET daysPaid = " + daysPaid + " WHERE player = '"
				+ warp.getOwner().getName() + "' AND warpName = '" + warp.getName() + "';";
		updateAsync(update);
	}

	@Override
	public void decreaseRentDays() {
		decreaseRentDays(1);
	}

	@Override
	public void decreaseRentDays(int daysPaid) {
		String update = "UPDATE " + table + " SET daysPaid = daysPaid - " + daysPaid + ";";
		updateAsync(update);
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<PlayerWarp> getAllWarps() {
		String query = "SELECT location, warpName, lore1, lore2, lore3, daysPaid, player FROM " + table;
		List<SQLRow> result = query(query);

		List<PlayerWarp> warps = new ArrayList<PlayerWarp>();
		for (SQLRow row : result)
			warps.add(new PlayerWarp(Bukkit.getOfflinePlayer(row.get(6)), WorldUtils.locationFromString(row.get(0)),
					row.get(1), getLores(row, 2), Integer.parseInt(row.get(5))));

		return warps;
	}
}
