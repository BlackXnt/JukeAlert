package com.untamedears.JukeAlert;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.untamedears.JukeAlert.command.CommandHandler;
import com.untamedears.JukeAlert.command.commands.ClearCommand;
import com.untamedears.JukeAlert.command.commands.ConfigCommand;
import com.untamedears.JukeAlert.command.commands.GroupCommand;
import com.untamedears.JukeAlert.command.commands.HelpCommand;
import com.untamedears.JukeAlert.command.commands.InfoCommand;
import com.untamedears.JukeAlert.command.commands.JaCommand;
import com.untamedears.JukeAlert.command.commands.JaListCommand;
import com.untamedears.JukeAlert.command.commands.JaMuteCommand;
import com.untamedears.JukeAlert.command.commands.JaToggleLeversCommand;
import com.untamedears.JukeAlert.command.commands.LookupCommand;
import com.untamedears.JukeAlert.command.commands.NameCommand;
import com.untamedears.JukeAlert.group.GroupMediator;
import com.untamedears.JukeAlert.listener.ItemExchangeListener;
import com.untamedears.JukeAlert.listener.JukeAlertListener;
import com.untamedears.JukeAlert.listener.MercuryListener;
import com.untamedears.JukeAlert.manager.ConfigManager;
import com.untamedears.JukeAlert.manager.PlayerManager;
import com.untamedears.JukeAlert.manager.SnitchManager;
import com.untamedears.JukeAlert.storage.JukeAlertLogger;
import com.untamedears.JukeAlert.util.RateLimiter;

public class JukeAlert extends JavaPlugin {

    private static JukeAlert instance;
    private JukeAlertLogger jaLogger;
    private ConfigManager configManager;
    private SnitchManager snitchManager;
    private PlayerManager playerManager;
    private CommandHandler commandHandler;
    private GroupMediator groupMediator;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        groupMediator = new GroupMediator();
        jaLogger = new JukeAlertLogger();
        snitchManager = new SnitchManager();
        playerManager = new PlayerManager();
        registerEvents();
        registerCommands();
        snitchManager.initialize();
        RateLimiter.initialize(this);
    }

    @Override
    public void onDisable() {
        snitchManager.saveSnitches();
    }

    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return commandHandler.dispatch(sender, label, args);
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JukeAlertListener(), this);
        if (pm.isPluginEnabled("ItemExchange"))
        	pm.registerEvents(new ItemExchangeListener(), this);
        if (pm.isPluginEnabled("Mercury"))
			pm.registerEvents(new MercuryListener(), this);
    }

    private void registerCommands() {
        commandHandler = new CommandHandler();
        commandHandler.addCommand(new InfoCommand());
        commandHandler.addCommand(new JaListCommand());
        commandHandler.addCommand(new NameCommand());
        commandHandler.addCommand(new ClearCommand());
        commandHandler.addCommand(new HelpCommand());
        commandHandler.addCommand(new JaCommand());
        commandHandler.addCommand(new GroupCommand());
        commandHandler.addCommand(new LookupCommand());
        commandHandler.addCommand(new JaMuteCommand());
        commandHandler.addCommand(new ConfigCommand());
        commandHandler.addCommand(new JaToggleLeversCommand());
    }

    public static JukeAlert getInstance() {
        return instance;
    }

    public JukeAlertLogger getJaLogger() {
        return jaLogger;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public SnitchManager getSnitchManager() {
        return snitchManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GroupMediator getGroupMediator() {
        return groupMediator;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    //Logs a message with the level of Info.
    public void log(String message) {
        this.getLogger().log(Level.INFO, message);
    }
}
