
package jdz.pwarp.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import jdz.bukkitUtils.misc.Config;
import jdz.pwarp.PlayerWarpPlugin;

public class WarpConfig {
	public static int warpCost = 100000;
	public static List<String> disabledWorlds = new ArrayList<String>();
	
	public static int loreLineWidth = 30;
	public static int maxLoreSize = 90;
	
	public static boolean log = true;
	
	public static String returnCommand = "";
	
	public static boolean safeWarpEnabled = true;
	public static List<Integer> unsafeBlocks = new ArrayList<Integer>();
	
	public static boolean rentEnabled = true;
	public static double rentCost = 10000;
	public static int rentFreeDays = 5;
	public static int rentMaxDays = 50;
	
	public static int teleportCooldown = 10;
	public static int teleportWarmUp = 5;
	public static boolean cancelOnMovement = true;
	
	public static boolean GPEnabled = false;
	public static boolean PSEnabled = false;
	public static boolean ASEnabled = false;
	
	
	public static void reloadConfig(){
		FileConfiguration config = Config.getConfig(PlayerWarpPlugin.instance);
		
		warpCost = config.getInt("Settings.warpCost");
		disabledWorlds = config.getStringList("Settings.disabledWorlds");
		
		maxLoreSize = config.getInt("Settings.maxLoreSize");
		loreLineWidth = maxLoreSize/3;
		
		log = config.getBoolean("Settings.log");
		
		returnCommand = config.getString("Settings.returnCommand");
		
		safeWarpEnabled = config.getBoolean("SafeWarp.enabled");
		unsafeBlocks = config.getIntegerList("SafeWarp.unsafeBlocks");
		
		rentEnabled = config.getBoolean("Rent.enabled");
		rentCost = config.getDouble("Rent.rentCost");
		rentMaxDays = config.getInt("Rent.maxDays");
		rentFreeDays = config.getInt("Rent.freeDays");
		
		teleportCooldown = config.getInt("Teleport.cooldown");
		teleportWarmUp = config.getInt("Teleport.warmup");
		cancelOnMovement = config.getBoolean("Teleport.cancelOnMovement");

		GPEnabled = config.getBoolean("GriefPrevention.enabled");
		PSEnabled = config.getBoolean("PlotSquared.enabled");
		ASEnabled = config.getBoolean("ASkyBlock.enabled");
	}
}
