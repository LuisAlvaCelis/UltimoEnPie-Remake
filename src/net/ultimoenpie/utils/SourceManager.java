package net.ultimoenpie.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.ultimoenpie.main.MainClass;

public class SourceManager {

	private MainClass plugin;
	private File file;
	private YamlConfiguration yml;
	private static SourceManager instance;
	private boolean statusStart;
	
	public SourceManager() {
		this.plugin=MainClass.getInstance();
		this.file=new File(plugin.getDataFolder()+File.separator+"database.yml");
		this.yml=YamlConfiguration.loadConfiguration(file);
	}
	
	public static SourceManager getInstance() {
		if(instance==null) {
			instance=new SourceManager();
		}
		return instance;
	}
	
	public Location getSpawnRandomLocation(String world) {
		Random random=new Random();
		int value=(int)Bukkit.getWorld(world).getWorldBorder().getSize();
		int x=random.nextInt(value * 2) - value;
		int z=random.nextInt(value * 2) - value;
		return new Location(Bukkit.getWorld(world), x, Bukkit.getWorld(world).getHighestBlockYAt(x,z), z);
	}
	
	public boolean getStatusAggressive(String mob) {
		return MainClass.getInstance().getConfig().getBoolean("Game.mobs.stats."+mob+".aggressiveness");
	}
	
	public void toggleAggressiveness(String mob) {
		if(MainClass.getInstance().getConfig().getBoolean("Game.mobs.stats."+mob+".aggressiveness")==false) {
			MainClass.getInstance().getConfig().set("Game.mobs.stats."+mob+".aggressiveness",true);
			MainClass.getInstance().saveConfig();
		}else {
			MainClass.getInstance().getConfig().set("Game.mobs.stats."+mob+".aggressiveness",false);
			MainClass.getInstance().saveConfig();
		}
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getMobHead(String mob) {
		String mobtexture=getTextureMob(mob);
		SkullMeta meta=(SkullMeta)plugin.getServer().getItemFactory().getItemMeta(Material.PLAYER_HEAD);
		String uuid=plugin.getConfig().getString("Game.mobs.stats."+mob+".UUID");
		GameProfile profile=new GameProfile(UUID.fromString(uuid), null);
		profile.getProperties().put("textures", new Property("textures", mobtexture));
		Field pf=null;
		try {
			pf=meta.getClass().getDeclaredField("profile");
			pf.setAccessible(true);
			pf.set(meta, profile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ItemStack is=new ItemStack(Material.PLAYER_HEAD,1,(short)3);
		if(mob.equalsIgnoreCase("MOOSHROOM")) {
			meta.setDisplayName(SourceCode.translateColor("&aMOOSHROOM"));
		}else {
			meta.setDisplayName(SourceCode.translateColor("&a"+mob));
		}
		is.setItemMeta(meta);
		return is;
	}
	
	public String getTextureMob(String mobname) {
		return this.plugin.getConfig().getString("Game.mobs.stats."+mobname+".texture");
	}
	
	public List<String> getPassiveMobs(){
		return this.plugin.getConfig().getStringList("Game.mobs.passive-mobs");
	}
	
	public List<String> getNeutralMobs(){
		return this.plugin.getConfig().getStringList("Game.mobs.neutral-mobs");
	}
	
	public List<String> getHostileMobs(){
		return this.plugin.getConfig().getStringList("Game.mobs.hostile-mobs");
	}
	
	public List<String> getBossMobs(){
		return this.plugin.getConfig().getStringList("Game.mobs.boss-mobs");
	}
	
	public YamlConfiguration getDBFile() {
		return this.yml;
	}
	
	public boolean isPVPStatus() {
		return MainClass.getInstance().getConfig().getBoolean("Game.pvp-status");
	}
	
	public void setPVPStatus(boolean status) {
		MainClass.getInstance().getConfig().set("Game.pvp-status", status);
		MainClass.getInstance().saveConfig();
	}
	
	public String getPrefix() {
		return this.plugin.getConfig().getString("Game.message.prefix");
	}
	
	public void setStatusStart(boolean status) {
		this.statusStart=status;
	}
	
	public boolean isStatusStartEnabled() {
		return this.statusStart;
	}
	
	public String getMessageNOPermission(String lang) {
		String msg=null;
		if(lang.equalsIgnoreCase("EN")) {
			msg=plugin.getConfig().getString("Game.message.format-nopermission-msg.english");
		}else if(lang.equalsIgnoreCase("ES")) {
			msg=plugin.getConfig().getString("Game.message.format-nopermission-msg.spanish");
		}
		return SourceCode.translateColor(msg);
	}
	
	public void sendMessageQuitToAll(Player player) {
		for(Player players:Bukkit.getServer().getOnlinePlayers()) {
			if(players!=null) {
				if(getLanguagePlayer(player).equalsIgnoreCase("EN")) {
					players.sendMessage(SourceCode.translateColor(plugin.getConfig().getString("Game.message.format-quit-msg-toall.english").replace("{player}", player.getName())));
				}else if(getLanguagePlayer(player).equalsIgnoreCase("ES")) {
					players.sendMessage(SourceCode.translateColor(plugin.getConfig().getString("Game.message.format-quit-msg-toall.spanish").replace("{player}", player.getName())));
				}				
			}
		}
	}
	
	public void sendMessageJoinToAll(Player player) {
		for(Player players:Bukkit.getServer().getOnlinePlayers()) {
			if(players!=null) {
				if(getLanguagePlayer(player).equalsIgnoreCase("EN")) {
					players.sendMessage(SourceCode.translateColor(plugin.getConfig().getString("Game.message.format-join-msg-toall.english").replace("{player}", player.getName())));
				}else if(getLanguagePlayer(player).equalsIgnoreCase("ES")) {
					players.sendMessage(SourceCode.translateColor(plugin.getConfig().getString("Game.message.format-join-msg-toall.spanish").replace("{player}", player.getName())));
				}
			}
		}
	}
	
	public void sendMessageJoin(Player player) {
		if(getLanguagePlayer(player).equalsIgnoreCase("EN")) {
			for(String msg:plugin.getConfig().getStringList("Game.message.format-join-msg.english")) {
				player.sendMessage(SourceCode.translateColor(msg));
			}
		}else if(getLanguagePlayer(player).equalsIgnoreCase("ES")) {
			for(String msg:plugin.getConfig().getStringList("Game.message.format-join-msg.spanish")) {
				player.sendMessage(SourceCode.translateColor(msg));
			}
		}
	}
	
	public String getKickMSG(String languaje) {
		String msg="";
		if(languaje.equalsIgnoreCase("EN")) {
			for(String txt: plugin.getConfig().getStringList("Game.message.format-kick-msg.english")) {
				msg=msg+" "+txt+"\n";
			}
		}else if(languaje.equalsIgnoreCase("ES")) {
			for(String txt: plugin.getConfig().getStringList("Game.message.format-kick-msg.spanish")) {
				msg=msg+" "+txt+"\n";
			}
		}
		return SourceCode.translateColor(msg);
	}
	
	public void setLanguagePlayer(Player player,String lang) {
		this.yml.set("Stats-players."+player.getUniqueId()+".language", lang);
		this.saveFileDBYML();
	}
	
	public String getLanguagePlayer(Player player) {
		return this.yml.getString("Stats-players."+player.getUniqueId()+".language");
	}
	
	public void addPlayerToListPlayers(Player player,String lang){
		List<String> list=yml.getStringList("List-players");
		list.add(player.getUniqueId().toString());
		this.yml.set("List-players", list);
		this.yml.set("Stats-players."+player.getUniqueId()+".language",lang);
		this.yml.set("Stats-players."+player.getUniqueId()+".name", player.getName());
		this.saveFileDBYML();
	}
	
	public boolean isWLStatusEnabled() {
		return this.yml.getBoolean("WL.status");
	}
	
	public void addPlayerToWL(OfflinePlayer player) {
		List<String> list=yml.getStringList("WL.list");
		list.add(player.getUniqueId().toString());
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		this.yml.set("WL.list", list);
		this.yml.set("WL.stats."+player.getUniqueId()+".name", player.getName());
		this.yml.set("WL.stats."+player.getUniqueId()+".date", sdf.format(Calendar.getInstance().getTime()));
		this.saveFileDBYML();
	}
	
	public void removePlayerFromWL(OfflinePlayer player) {
		List<String> list=yml.getStringList("WL.list");
		list.remove(player.getUniqueId().toString());
		this.yml.set("WL.list", list);
		this.yml.set("WL.stats."+player.getUniqueId(), null);
		this.saveFileDBYML();
	}
	
	public void setWLStatus(boolean status) {
		this.yml.set("WL.status", status);
		this.saveFileDBYML();
	}
	
	public List<String> getListPlayers(){
		return this.yml.getStringList("List-players");
	}
	
	public List<String> getListPlayersWL() {
		return this.yml.getStringList("WL.list");
	}
	
	public void saveFileDBYML() {
		try {
			this.yml.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
