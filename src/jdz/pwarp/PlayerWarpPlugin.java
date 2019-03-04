
package jdz.pwarp;

import org.bukkit.plugin.java.JavaPlugin;

import jdz.bukkitUtils.components.events.custom.ConfigReloadEvent;
import jdz.bukkitUtils.fileIO.FileLogger;
import jdz.pwarp.commands.AdminCommandExecutor;
import jdz.pwarp.commands.PlayerCommandExecutor;
import jdz.pwarp.config.RentConfig;
import jdz.pwarp.config.SafeWarpConfig;
import jdz.pwarp.config.TeleportConfig;
import jdz.pwarp.config.WarpConfig;
import jdz.pwarp.data.WarpManager;
import jdz.pwarp.eventListeners.PlayerWarpListeners;
import jdz.pwarp.gui.PlayerWarpGuiMenu;
import jdz.pwarp.tasks.RentCheckerTask;
import lombok.Getter;

public class PlayerWarpPlugin extends JavaPlugin {
	@Getter private static PlayerWarpPlugin instance;
	public static FileLogger logger;

	@Override
	public void onEnable() {
		instance = this;
		logger = new FileLogger(this);

		registerConfig();
		new ConfigReloadEvent(this).call();

		WarpManager.getInstance();

		PlayerWarpListeners.register(this);

		new RentCheckerTask(this);

		new PlayerCommandExecutor().register();
		new AdminCommandExecutor().register();

		new PlayerWarpGuiMenu(this);
	}

	private void registerConfig() {
		new RentConfig(this).register();
		new SafeWarpConfig(this).register();
		new TeleportConfig(this).register();
		new WarpConfig(this).register();
	}

}
