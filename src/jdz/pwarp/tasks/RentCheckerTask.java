package jdz.pwarp.tasks;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import jdz.bukkitUtils.fileIO.FileLogger;
import jdz.bukkitUtils.misc.Config;
import jdz.bukkitUtils.misc.TimedTask;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.WarpManager;

public class RentCheckerTask{
	public static RentCheckerTask instance;
	private Date nextCheck = getNextCheck(getLastCheck(), 24, 0);
	private TimedTask task;
	
	public RentCheckerTask(PlayerWarpPlugin plugin) {
		if (instance == null) {
			task = new TimedTask(plugin, 1200, ()->{
					if (new Date().after(nextCheck)){
						Bukkit.broadcastMessage("Sys: "+new Date());
						Bukkit.broadcastMessage("next: "+nextCheck);
						WarpManager.getInstance().decreaseRentDays();
						setLastCheck(new Date());
						Bukkit.broadcastMessage("new next: "+nextCheck);
					}
				});
			task.stop();
			PlayerWarpPlugin.sqlApi.runOnConnect(()->{task.start();});
			instance = this;
		}
	}
	
	public Date getLastCheck(){
		FileConfiguration config = Config.getConfig(PlayerWarpPlugin.instance);
		if (config.contains("Rent.lastCheck"))
			return new Date(config.getLong("Rent.lastCheck"));
		else
			return new Date();
	}
	
	public void setLastCheck(Date lastCheck){
		nextCheck = getNextCheck(lastCheck, 24, 0);
		try {
			FileConfiguration config = Config.getConfig(PlayerWarpPlugin.instance);
			config.set("Rent.lastCheck", lastCheck.getTime());
			config.save(Config.getConfigFile(PlayerWarpPlugin.instance));
		} catch (IOException e) {
			new FileLogger(PlayerWarpPlugin.instance).createErrorLog(e);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private Date getNextCheck(Date date, int hours, int minutes){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		if (date.getHours() > hours || date.getHours() == hours && date.getMinutes() > minutes)
			c.add(Calendar.DATE, 1);
		
		Date nextDate = c.getTime();
		nextDate.setHours(hours);
		nextDate.setMinutes(minutes);
		return nextDate;
	}
}
