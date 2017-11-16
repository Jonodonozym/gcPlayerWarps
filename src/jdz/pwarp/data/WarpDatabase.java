
package jdz.pwarp.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import jdz.bukkitUtils.fileIO.FileLogger;
import jdz.bukkitUtils.misc.WorldUtils;
import jdz.bukkitUtils.sql.SqlApi;
import jdz.bukkitUtils.sql.SqlColumn;
import jdz.bukkitUtils.sql.SqlColumnType;
import jdz.bukkitUtils.vault.VaultLoader;
import jdz.pwarp.PlayerWarpPlugin;


public class WarpDatabase {
	public static WarpDatabase instance;
	private final SqlApi api;

	private static final String table = "gcPlayerWarps";
	private static final SqlColumn[] columns = new SqlColumn[] { new SqlColumn("player", SqlColumnType.STRING_64),
			new SqlColumn("warpName", SqlColumnType.STRING_64), 
			new SqlColumn("location", SqlColumnType.STRING_256),
			new SqlColumn("lore1", SqlColumnType.STRING_256),
			new SqlColumn("lore2", SqlColumnType.STRING_256),
			new SqlColumn("lore3", SqlColumnType.STRING_256),
			new SqlColumn("daysPaid", SqlColumnType.INT_2_BYTE) };

	public WarpDatabase(PlayerWarpPlugin plugin, SqlApi api) {
		this.api = api;
		if (instance == null) {
			api.runOnConnect(()->{
				api.addTable(table, columns);
			});
			instance = this;
		}
	}
	
	public boolean exists(PlayerWarp warp){
		String query = "SELECT player FROM " + table + " WHERE player = '" + warp.getOwner().getName()
				+ "' AND warpName = '" + warp.getName() + "';";
		return (!api.getRows(query).isEmpty());
	}
	
	public void setWarp(PlayerWarp warp){
		setWarp(warp, warp.getLocation());
	}

	/**
	 * Creates a new warp if one doesn't exist, otherwise sets the existing warps location to the given warp's location
	 * @param warp
	 */
	public void setWarp(PlayerWarp warp, Location l){		
		String locationString = WorldUtils.locationToString(l);
		List<String> fullLore = new ArrayList<String>(warp.getLore());
		while(fullLore.size() < 3)
			fullLore.add("");
		
		if (!exists(warp)) {
			String update = "INSERT INTO " + table + " (player, location, warpName, lore1, lore2, lore3, daysPaid) VALUES('"
					+ warp.getOwner().getName() + "','" + locationString + "','" + warp.getName() + "','"
					+ fullLore.get(0) + "','" +fullLore.get(1)+ "','" + fullLore.get(2) + "'," +
					WarpConfig.rentFreeDays + ");";
			api.executeUpdate(update);
		}
		else{
			String update = "UPDATE " + table + " SET location = '"+locationString+"';";
			api.executeUpdate(update);
		}
	}

	public void delWarp(PlayerWarp warp){
		String update = "DELETE FROM " + table + " WHERE player = '"+warp.getOwner().getName()+"' AND warpName = '"+warp.getName()+"';";
		api.executeUpdate(update);
	}

	public void delWarp(OfflinePlayer player, String warpName){
		String update = "DELETE FROM " + table + " WHERE player = '"+player.getName()+"' AND warpName = '"+warpName+"';";
		api.executeUpdate(update);
	}
	
	public int getNumWarps(OfflinePlayer player){
		String query = "SELECT COUNT(*) FROM "+table+" WHERE player = '"+player.getName()+"';";
		List<String[]> result = api.getRows(query);
		if (result.isEmpty())
			return 0;
		return Integer.parseInt(result.get(0)[0]);
	}

	public PlayerWarp getWarp(OfflinePlayer player, String warpName){
		return getWarp(player.getName(), warpName);
	}
	
	public PlayerWarp getWarp(String player, String warpName){
		String query = "SELECT location, lore1, lore2, lore3, daysPaid FROM "+table+" WHERE player = '"+player+"' AND warpName = '"+warpName+"';";
		List<String[]> result = api.getRows(query);
		
		if (result.isEmpty())
			return null;
		
		@SuppressWarnings("deprecation")
		OfflinePlayer owner = Bukkit.getOfflinePlayer(player);
		Location l = WorldUtils.locationFromString(result.get(0)[0]);
		List<String> lore = getLores(result.get(0), 1);
		return new PlayerWarp(owner, l, warpName, lore, Integer.parseInt(result.get(0)[4]));
	}
	
	private List<String> getLores(String[] result, int startIndex){
		String lore1 = result[startIndex++];
		String lore2 = result[startIndex++];
		String lore3 = result[startIndex];
		int size = lore3.equals("") ? lore2.equals("") ? lore1.equals("")? 0:1:2:3;
		List<String> lore = new ArrayList<String>(size);
		if (size > 2)
			lore.add(lore3);
		if (size > 1)
			lore.add(lore2);
		if (size > 0)
			lore.add(lore1);
		return lore;
	}
	
	public List<PlayerWarp> getWarps(OfflinePlayer player){
		String query = "SELECT location, warpName, lore1, lore2, lore3, daysPaid FROM "+table+" WHERE player = '"+player.getName()+"';";
		List<String[]> result = api.getRows(query);
		
		List<PlayerWarp> warps = new ArrayList<PlayerWarp>();
		for (String[] s: result)
			warps.add(new PlayerWarp(player, WorldUtils.locationFromString(s[0]), s[1], getLores(s, 2), Integer.parseInt(s[5])));
		return warps;
	}
	
	public PlayerWarp getFirstWarp(OfflinePlayer player){
		String query = "SELECT location, warpName, lore1, lore2, lore3, daysPaid FROM "+table+" WHERE player = '"+player.getName()+"' LIMIT 1;";
		List<String[]> result = api.getRows(query);
		if (result.isEmpty())
			return null;
		String[] s = result.get(0);
		return new PlayerWarp(player, WorldUtils.locationFromString(s[0]), s[1], getLores(s, 2), Integer.parseInt(s[5]));
	}
	
	public void rename(PlayerWarp warp, String newName) {
		String update = "UPDATE "+table+" SET warpName = '"+newName+"' WHERE player = '"+warp.getOwner().getName()+"' AND warpName = '"+warp.getName()+"';";
		api.executeUpdate(update);
	}
	
	public void setLore(OfflinePlayer player, String warpName, String lore, int index){
		if (index < 1 || index > 3){
			new FileLogger(PlayerWarpPlugin.instance).createErrorLog(new RuntimeException("Invalid lore index "+index+", should be between 1 and 3"));
			return;
		}
		
		String update = "UPDATE "+table+" SET lore"+index+" = '"+lore+"' WHERE player = '"+player.getName()+"' AND warpName = '"+warpName+"';";
		api.executeUpdate(update);
	}
	
	public void setRentDays(OfflinePlayer offlinePlayer, String warpName, int daysPaid) {
		if (daysPaid > WarpConfig.rentMaxDays)
			daysPaid = WarpConfig.rentMaxDays;

		String update = "UPDATE " + table + " SET daysPaid = " + daysPaid + " WHERE player = '"
				+ offlinePlayer.getName() + "' AND warpName = '" + warpName + "';";
		api.executeUpdate(update);
	}

	public void decreaseRentDays() {
		decreaseRentDays(1);
	}

	public void decreaseRentDays(int daysPaid) {
		String update = "UPDATE " + table + " SET daysPaid = daysPaid - " + daysPaid + ";";
		api.executeUpdate(update);
	}

	@SuppressWarnings("deprecation")
	public List<PlayerWarp> getOverdueRents() {
		String query = "SELECT location, warpName, lore1, lore2, lore3, daysPaid, player FROM " + table + " WHERE daysPaid <= 0";
		List<String[]> result = api.getRows(query);
		
		List<PlayerWarp> warps = new ArrayList<PlayerWarp>();
		for (String[] s: result)
			warps.add(new PlayerWarp(Bukkit.getOfflinePlayer(s[6]), WorldUtils.locationFromString(s[0]), s[1], getLores(s, 2), Integer.parseInt(s[5])));

		return warps;
	}
	
	@SuppressWarnings("deprecation")
	public List<PlayerWarp> getAllWarps() {
		String query = "SELECT location, warpName, lore1, lore2, lore3, daysPaid, player FROM " + table;
		List<String[]> result = api.getRows(query);
		
		List<PlayerWarp> warps = new ArrayList<PlayerWarp>();
		for (String[] s: result)
			warps.add(new PlayerWarp(Bukkit.getOfflinePlayer(s[6]), WorldUtils.locationFromString(s[0]), s[1], getLores(s, 2), Integer.parseInt(s[5])));

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
}
