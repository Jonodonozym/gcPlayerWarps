package jdz.pwarp.tasks;

import java.util.Calendar;
import java.util.Date;

import org.bukkit.Bukkit;

import jdz.bukkitUtils.configuration.AutoConfig;
import jdz.bukkitUtils.configuration.Config;
import jdz.bukkitUtils.configuration.NotConfig;
import jdz.pwarp.PlayerWarpPlugin;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.data.WarpManager;
import lombok.Getter;

public class RentCheckerTask extends AutoConfig {
	@NotConfig @Getter private static RentCheckerTask instance;
	private Date lastCheck = new Date();

	public RentCheckerTask(PlayerWarpPlugin plugin) {
		super(plugin);
		register();
		reloadConfig();

		WarpDatabase.getInstance().runOnConnect(() -> {
			Bukkit.getScheduler().runTaskTimer(plugin, () -> {
				if (new Date().after(getNextCheck(lastCheck, 24, 0))) {
					setLastCheck(new Date());
					if (Config.getConfig(plugin).getBoolean("Rent.enabled"))
						WarpManager.getInstance().decreaseRentDays();
				}
			}, 20, 20);
		});
	}

	public void setLastCheck(Date lastCheck) {
		this.lastCheck = lastCheck;
		saveChanges();
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
