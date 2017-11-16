
package jdz.pwarp;

import org.bukkit.plugin.java.JavaPlugin;

import jdz.bukkitUtils.fileIO.FileLogger;
import jdz.bukkitUtils.sql.SqlApi;
import jdz.bukkitUtils.sql.SqlMessageQueue;
import jdz.pwarp.commands.AdminCommandExecutor;
import jdz.pwarp.commands.PlayerCommandExecutor;
import jdz.pwarp.data.WarpConfig;
import jdz.pwarp.data.WarpDatabase;
import jdz.pwarp.eventListeners.PlayerWarpListeners;
import jdz.pwarp.gui.PlayerWarpGuiMenu;
import jdz.pwarp.tasks.RentCheckerTask;

public class PlayerWarpPlugin extends JavaPlugin{
	public static SqlMessageQueue sqlMessageQueue;
	public static SqlApi sqlApi;
	public static PlayerWarpPlugin instance;
	public static FileLogger logger;
	
	@Override
	public void onEnable(){
		instance = this;
		logger = new FileLogger(this);
		
		WarpConfig.reloadConfig();
		
		sqlApi = new SqlApi(this);
		sqlMessageQueue = new SqlMessageQueue(this, sqlApi);
		new WarpDatabase(this, sqlApi);
		
		PlayerWarpListeners.register(this);
		
		new RentCheckerTask(this);
		
		new PlayerCommandExecutor().register();
		new AdminCommandExecutor().register();
		
		new PlayerWarpGuiMenu(this);
	}

}
