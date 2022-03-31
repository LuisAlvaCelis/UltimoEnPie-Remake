package net.ultimoenpie.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.ultimoenpie.commands.LanguageCommand;
import net.ultimoenpie.commands.UltimoEnPieCommand;
import net.ultimoenpie.entitys.CustomBAT;
import net.ultimoenpie.entitys.CustomEntityType;
import net.ultimoenpie.entitys.CustomZombie;
import net.ultimoenpie.entitys.SpawnCMD;
import net.ultimoenpie.listeners.Listeners;
import net.ultimoenpie.utils.SourceCode;

public class MainClass extends JavaPlugin {
	
	private static MainClass instance;
	
	public static MainClass getInstance() {
		if(instance==null) {
			instance=new MainClass();
		}
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance=this;
		new CustomEntityType().register("CustomZmbie","Zombie", CustomZombie.class);
		new CustomEntityType().register("CustomBat", "Bat", CustomBAT.class);
		this.registerYML();
		this.registerEvents();
		this.registerCommands();
		this.getServer().getConsoleSender().sendMessage(SourceCode.translateColor("&aUltimoEnPie-Remake has been enabled."));
	}
	
	@Override
	public void onDisable() {
		instance=null;
		this.getServer().getConsoleSender().sendMessage(SourceCode.translateColor("&cUltimoEnPie-Remake has been disabled."));
	}
	
	private void registerEvents() {
		PluginManager pm=Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Listeners(this), this);
	}
	
	private void registerCommands() {
		this.getCommand("language").setExecutor(new LanguageCommand());
		this.getCommand("ultimoenpie").setExecutor(new UltimoEnPieCommand(this));
		this.getCommand("spawn").setExecutor(new SpawnCMD());
	}
	
	private void registerYML() {
		try {
			File f1=new File(getDataFolder(),"config.yml");
			File f2=new File("plugins/UltimoEnPie-Remake/database.yml");
			if(!f1.exists()) {
				this.getConfig().options().copyDefaults(true);
				this.saveConfig();
				if(!f2.exists()) {
					f2.createNewFile();
					this.saveResource("database.yml", true);
				}
			}else if(!f2.exists()) {
				f2.createNewFile();
				this.saveResource("database.yml", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
