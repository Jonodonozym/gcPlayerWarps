package jdz.pwarp.tasks;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;

import jdz.bukkitUtils.fileIO.FileLogger;
import jdz.bukkitUtils.misc.Config;
import jdz.bukkitUtils.misc.TimedTask;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.data.WarpManager;

public class RentCheckerTask {
	public static RentCheckerTask instance;
	private Date nextCheck = getNextCheck(getLastCheck(), 24, 0);
	private TimedTask task;

	public RentCheckerTask(PlayerWarpPlugin plugin) {
		if (instance == null) {
			task = new TimedTask(plugin, 1200, () -> {
				if (new Date().after(nextCheck)) {
					setLastCheck(new Date());

					if (Config.getConfig(plugin).getBoolean("Rent.enabled"))
						WarpManager.getInstance().decreaseRentDays();
				}
			});
			task.stop();
			WarpDatabase.runOnConnect(() -> {
				task.start();
			});
			instance = this;
		}
	}

	public Date getLastCheck() {
		FileConfiguration config = Config.getConfig(PlayerWarpPlugin.getInstance());
		if (config.contains("Rent.lastCheck"))
			return new Date(config.getLong("Rent.lastCheck"));
		else
			return new Date();
	}

	public void setLastCheck(Date lastCheck) {
		nextCheck = getNextCheck(lastCheck, 24, 0);
		try {
			FileConfiguration config = Config.getConfig(PlayerWarpPlugin.getInstance());
			config.set("Rent.lastCheck", lastCheck.getTime());
			config.save(Config.getConfigFile(PlayerWarpPlugin.getInstance()));
		}
		catch (IOException e) {
			new FileLogger(PlayerWarpPlugin.getInstance()).createErrorLog(e);
		}
	}


	@SuppressWarnings("deprecation")
	private Date getNextCheck(Date date, int hours, int minutes) {
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
