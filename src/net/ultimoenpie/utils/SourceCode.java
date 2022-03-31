package net.ultimoenpie.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.server.v1_16_R1.PathfinderGoalSelector;
import net.ultimoenpie.apis.anvilgui.AnvilGUI;
import net.ultimoenpie.main.MainClass;


public class SourceCode {
	
	public static void openGUIMob(Player player,String mob) {
		HashMap<Integer, ItemStack> hm=new HashMap<>();
		if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
			if(mob.equals("BAT")) {
				if(MainClass.getInstance().getConfig().getBoolean("Game.mobs.stats."+mob+".aggressiveness")==false) {
					hm.put(0, createItemNoAttributesNoLore(1,Material.DIAMOND_SWORD, 0, "&eBehavior", Arrays.asList(new String[] {translateColor("&bCurrent status: pacific")})));
				}else {
					hm.put(0, createItemNoAttributesNoLore(1,Material.DIAMOND_SWORD, 0, "&eBehavior", Arrays.asList(new String[] {translateColor("&bCurrent status: aggressive")})));
				}
			}
			hm.put(17, createItemNoLore(Material.GRAY_CARPET, 0, "&7Return"));
			player.openInventory(createInventory(18,"&2"+mob, hm));
		}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
			if(mob.equals("BAT")) {
				if(MainClass.getInstance().getConfig().getBoolean("Game.mobs.stats."+mob+".aggressiveness")==false) {
					hm.put(0, createItemNoAttributesNoLore(1,Material.DIAMOND_SWORD, 0, "&eBehavior", Arrays.asList(new String[] {translateColor("&bEstado actual: pacifico")})));
				}else {
					hm.put(0, createItemNoAttributesNoLore(1,Material.DIAMOND_SWORD, 0, "&eComportamiento", Arrays.asList(new String[] {translateColor("&bEstado actual: agresivo")})));
				}
			}
			hm.put(17, createItemNoLore(Material.GRAY_CARPET, 0, "&7Regresar"));
			player.openInventory(createInventory(18,"&2"+mob, hm));
		}
		
	}
	
	public static Inventory createInventory(int size,String title,HashMap<Integer, ItemStack> hm) {
		Inventory inv=MainClass.getInstance().getServer().createInventory(null, size,translateColor(title));
		for(Integer key:hm.keySet()) {
			inv.setItem(key, hm.get(key));
		}
		return inv;
	}
	
	public static void openGUI(Player player,int num) {
		HashMap<Integer, ItemStack> hm=new HashMap<>();
		if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
			switch(num) {
			case 1:
				hm.clear();
				hm.put(3, createItem(Material.LIME_TERRACOTTA,0, "&bEnglish", Arrays.asList(new String[] {translateColor("&7 All server msgs will be"),translateColor("&7 translated into English")})));
				hm.put(5, createItem(Material.RED_TERRACOTTA,0, "&bEspañol", Arrays.asList(new String[] {translateColor("&7 Todos los mensaje del servidor serán"),translateColor("&7 traducidos al Español")})));
				player.openInventory(createInventory(9, "&cLanguage / Idioma", hm));
				break;
			case 2:
				hm.clear();
				hm.put(0, createItemNoAttributesNoLore(1,Material.DIAMOND_SWORD, 0, "&aPvP", Arrays.asList(new String[] {checkItemPVPStatusEN()})));
				hm.put(1, createItemHead(player.getName(), "&aWhitelist", Arrays.asList(new String[] {translateColor("&7Add/Remove/Look/Configure")})));
				hm.put(2, createItem(Material.WITHER_SKELETON_SKULL, 0, "&aMobs Setting", Arrays.asList(new String[] {translateColor("&7Modify mobs statistics/attributes")})));
				player.openInventory(createInventory(27, "&c&lLast One Standing", hm));
				break;
			case 3:
				hm.clear();
				hm.put(0, createItem(Material.NAME_TAG, 0, "&aAdd", Arrays.asList(new String[] {translateColor("&7&oAdd new player to whitelist")})));
				hm.put(1, createItem(Material.SHEARS, 0, "&aRemove", Arrays.asList(new String[] {translateColor("&7&oRemove player from whitelist")})));
				hm.put(2, checkItemWLStatusEN());
				hm.put(3, createItemNoLore(Material.GRAY_CARPET, 0, "&7Return"));
				for(int i=9;i<18;i++) {
					hm.put(i, createItemNoLore(Material.RED_STAINED_GLASS_PANE, 0," "));
				}
				if(SourceManager.getInstance().getListPlayersWL().size()<=26) {
					for(int i=0;i<SourceManager.getInstance().getListPlayersWL().size();i++) {
						String owner=SourceManager.getInstance().getDBFile().getString("WL.stats."+SourceManager.getInstance().getListPlayersWL().get(i)+".name");
						hm.put((i+18), createItemHead(owner, "&a"+owner , Arrays.asList(new String[] {translateColor("&7Added on date: "),translateColor("&7  -"+SourceManager.getInstance().getDBFile().getString("WL.stats."+SourceManager.getInstance().getListPlayersWL().get(i)+".date"))})));
					}
				}else {
					for(int i=0;i<=26;i++) {
						String owner=SourceManager.getInstance().getDBFile().getString("WL.stats."+SourceManager.getInstance().getListPlayersWL().get(i)+".name");
						hm.put((i+18), createItemHead(owner, "&a"+owner , Arrays.asList(new String[] {translateColor("&7Added on date: "),translateColor("&7  -"+SourceManager.getInstance().getDBFile().getString("WL.stats."+SourceManager.getInstance().getListPlayersWL().get(i)+".date"))})));
					}
				}
				hm.put(53, createItemNoLore(Material.GRAY_CARPET, 0, "&7Next page"));
				player.openInventory(createInventory(54,"&3&lWhitelist", hm));
				break;
			case 4:
				new AnvilGUI.Builder().onClose(pl->{openGUI(pl, 3);}).onComplete((pl, text) -> {pl.chat("/ultimoenpie wl add "+text);return AnvilGUI.Response.close();}).preventClose().text("Username").item(new ItemStack(Material.PAPER)).title("Enter name").plugin(MainClass.getInstance()).open(player);
				break;
			case 5:
				new AnvilGUI.Builder().onClose(pl->{openGUI(pl, 3);}).onComplete((pl, text) -> {pl.chat("/ultimoenpie wl remove "+text);return AnvilGUI.Response.close();}).preventClose().text("Username").item(new ItemStack(Material.PAPER)).title("Enter name").plugin(MainClass.getInstance()).open(player);
				break;
			case 6:
				hm.clear();
				hm.put(1, createItemNoAttributesNoLore(2,Material.WOODEN_SWORD, 0, "&3Passive mobs", null));
				hm.put(3, createItemNoAttributesNoLore(2,Material.GOLDEN_SWORD, 0, "&3Neutral mobs", null));
				hm.put(5, createItemNoAttributesNoLore(2,Material.IRON_SWORD, 0, "&3Hostile mobs", null));
				hm.put(7, createItemNoAttributesNoLore(2,Material.DIAMOND_SWORD, 0, "&3Boss mobs", null));
				hm.put(13, createItemNoLore(Material.GRAY_CARPET, 0, "&7Return"));
				player.openInventory(createInventory(18, "&8&lSelect mob type", hm));
				break;
			case 7:
				hm.clear();
				for(int i=0;i<SourceManager.getInstance().getPassiveMobs().size();i++) {
					hm.put(i, SourceManager.getInstance().getMobHead(SourceManager.getInstance().getPassiveMobs().get(i)));
				}
				hm.put(52, createItem(Material.COMPARATOR, 0, "&bBehavioral status for all: &e", Arrays.asList(new String[] {translateColor("&7&oChange the behavior mode"),translateColor("&7&oof all passive mobs.")})));
				hm.put(53, createItemNoLore(Material.GRAY_CARPET, 0, "&7Return"));
				player.openInventory(createInventory(54,"&3&lPassive mobs", hm));
				break;
			case 8:
				hm.clear();
				for(int i=0;i<SourceManager.getInstance().getNeutralMobs().size();i++) {
					hm.put(i, SourceManager.getInstance().getMobHead(SourceManager.getInstance().getNeutralMobs().get(i)));
				}
				hm.put(22, createItemNoLore(Material.GRAY_CARPET, 0, "&7Return"));
				player.openInventory(createInventory(27,"&3&lNeutral mobs", hm));
				break;
			case 9:
				hm.clear();
				for(int i=0;i<SourceManager.getInstance().getHostileMobs().size();i++) {
					hm.put(i, SourceManager.getInstance().getMobHead(SourceManager.getInstance().getHostileMobs().get(i)));
				}
				hm.put(35, createItemNoLore(Material.GRAY_CARPET, 0, "&7Return"));
				player.openInventory(createInventory(36, "&3&lHostile mobs", hm));
				break;
			case 10:
				hm.clear();
				for(int i=0;i<SourceManager.getInstance().getBossMobs().size();i++) {
					hm.put(i, SourceManager.getInstance().getMobHead(SourceManager.getInstance().getBossMobs().get(i)));
				}
				hm.put(8, createItemNoLore(Material.GRAY_CARPET, 0, "&7Return"));
				player.openInventory(createInventory(9, "&3&lBoss mobs", hm));
				break;
			default:
				sendMessageError(player);
			}
		}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
			switch(num) {
			case 1:
				hm.clear();
				hm.put(3, createItem(Material.RED_TERRACOTTA,0, "&bEnglish", Arrays.asList(new String[] {translateColor("&7 All server msgs will be"),translateColor("&7 translated into English")})));
				hm.put(5, createItem(Material.LIME_TERRACOTTA,0, "&bEspañol", Arrays.asList(new String[] {translateColor("&7 Todos los mensaje del servidor serán"),translateColor("&7 traducidos al Español")})));
				player.openInventory(createInventory(9, "&cLanguage / Idioma", hm));
				break;
			case 2:
				hm.clear();
				hm.put(0, createItemNoAttributesNoLore(1,Material.DIAMOND_SWORD, 0, "&aPvP", Arrays.asList(new String[] {checkItemPVPStatusES()})));
				hm.put(1, createItemHead(player.getName(), "&aLista blanca", Arrays.asList(new String[] {translateColor("&7Agregar/Remover/Mirar/Configurar")})));
				hm.put(2, createItem(Material.WITHER_SKELETON_SKULL, 0, "&aConfiguración de mobs", Arrays.asList(new String[] {translateColor("&7Modificar estadísticas/atributos de los mobs")})));
				player.openInventory(createInventory(27, "&c&lUltimo En Pie", hm));
				break;
			case 3:
				hm.clear();
				hm.put(0, createItem(Material.NAME_TAG, 0, "&aAgregar", Arrays.asList(new String[] {translateColor("&7&oAgregar nuevo jugador a la lista blanca")})));
				hm.put(1, createItem(Material.SHEARS, 0, "&aRemover", Arrays.asList(new String[] {translateColor("&7&oRemover jugador de la lista blanca")})));
				hm.put(2, checkItemWLStatusES());
				hm.put(3, createItemNoLore(Material.GRAY_CARPET, 0, "&7Regresar"));
				for(int i=9;i<18;i++) {
					hm.put(i, createItemNoLore(Material.RED_STAINED_GLASS_PANE, 0," "));
				}
				if(SourceManager.getInstance().getListPlayersWL().size()<=26) {
					for(int i=0;i<SourceManager.getInstance().getListPlayersWL().size();i++) {
						String owner=SourceManager.getInstance().getDBFile().getString("WL.stats."+SourceManager.getInstance().getListPlayersWL().get(i)+".name");
						hm.put((i+18), createItemHead(owner, "&a"+owner , Arrays.asList(new String[] {translateColor("&7Agregado en la fecha: "),translateColor("&7  - "+SourceManager.getInstance().getDBFile().getString("WL.stats."+SourceManager.getInstance().getListPlayersWL().get(i)+".date"))})));
					}
				}else {
					for(int i=0;i<=26;i++) {
						String owner=SourceManager.getInstance().getDBFile().getString("WL.stats."+SourceManager.getInstance().getListPlayersWL().get(i)+".name");
						hm.put((i+18), createItemHead(owner, "&a"+owner , Arrays.asList(new String[] {translateColor("&7Agregado en la fecha: "),translateColor("&7  - "+SourceManager.getInstance().getDBFile().getString("WL.stats."+SourceManager.getInstance().getListPlayersWL().get(i)+".date"))})));
					}
				}
				hm.put(53, createItemNoLore(Material.GRAY_CARPET, 0, "&7Siguente página"));
				player.openInventory(createInventory(54,"&3&lLista blanca", hm));
				break;
			case 4:
				new AnvilGUI.Builder().onClose(pl->{openGUI(pl, 3);}).onComplete((pl, text) -> {pl.chat("/ultimoenpie wl add "+text);return AnvilGUI.Response.close();}).preventClose().text("Usuario").item(new ItemStack(Material.PAPER)).title("Ingrese nombre").plugin(MainClass.getInstance()).open(player);
				break;
			case 5:
				new AnvilGUI.Builder().onClose(pl->{openGUI(pl, 3);}).onComplete((pl, text) -> {pl.chat("/ultimoenpie wl remove "+text);return AnvilGUI.Response.close();}).preventClose().text("Usuario").item(new ItemStack(Material.PAPER)).title("Ingrese nombre").plugin(MainClass.getInstance()).open(player);
				break;
			case 6:
				hm.clear();
				hm.put(1, createItemNoAttributesNoLore(2,Material.WOODEN_SWORD, 0, "&3Mobs pasivos", null));
				hm.put(3, createItemNoAttributesNoLore(2,Material.GOLDEN_SWORD, 0, "&3Mobs neutrales", null));
				hm.put(5, createItemNoAttributesNoLore(2,Material.IRON_SWORD, 0, "&3HMobs hostiles", null));
				hm.put(7, createItemNoAttributesNoLore(2,Material.DIAMOND_SWORD, 0, "&3Mobs jefes", null));
				hm.put(13, createItemNoLore(Material.GRAY_CARPET, 0, "&7Regresar"));
				player.openInventory(createInventory(18, "&8&lSeleccione tipo de mob", hm));
				break;
			case 7:
				hm.clear();
				for(int i=0;i<SourceManager.getInstance().getPassiveMobs().size();i++) {
					hm.put(i, SourceManager.getInstance().getMobHead(SourceManager.getInstance().getPassiveMobs().get(i)));
				}
				hm.put(52, createItem(Material.COMPARATOR, 0, "&bEstado de comportamiento para todos: &e", Arrays.asList(new String[] {translateColor("&7&oCambia el modo de comportamiento"),translateColor("&7&ode todos los mobs passivos.")})));
				hm.put(53, createItemNoLore(Material.GRAY_CARPET, 0, "&7Regresar"));
				player.openInventory(createInventory(54,"&3&lMobs pasivos", hm));
				break;
			case 8:
				hm.clear();
				for(int i=0;i<SourceManager.getInstance().getNeutralMobs().size();i++) {
					hm.put(i, SourceManager.getInstance().getMobHead(SourceManager.getInstance().getNeutralMobs().get(i)));
				}
				hm.put(22, createItemNoLore(Material.GRAY_CARPET, 0, "&7Regresar"));
				player.openInventory(createInventory(27,"&3&lMobs neutrales", hm));
				break;
			case 9:
				hm.clear();
				for(int i=0;i<SourceManager.getInstance().getHostileMobs().size();i++) {
					hm.put(i, SourceManager.getInstance().getMobHead(SourceManager.getInstance().getHostileMobs().get(i)));
				}
				hm.put(35, createItemNoLore(Material.GRAY_CARPET, 0, "&7Regresar"));
				player.openInventory(createInventory(36, "&3&lMobs hostiles", hm));
				break;
			case 10:
				hm.clear();
				for(int i=0;i<SourceManager.getInstance().getBossMobs().size();i++) {
					hm.put(i, SourceManager.getInstance().getMobHead(SourceManager.getInstance().getBossMobs().get(i)));
				}
				hm.put(8, createItemNoLore(Material.GRAY_CARPET, 0, "&7Regresar"));
				player.openInventory(createInventory(9, "&3&lMobs jefes", hm));
				break;
			default:
				sendMessageError(player);
			}
		}
	}
	
	public static String checkItemPVPStatusEN() {
		if(MainClass.getInstance().getConfig().getBoolean("Game.pvp-status")==false) {
			return translateColor("&bCurrent status: &cdisabled");
		}else {
			return translateColor("&bCurrent status: &aenabled");
		}
	}
	
	public static String checkItemPVPStatusES() {
		if(SourceManager.getInstance().isPVPStatus()==false) {
			return translateColor("&bEstado actual: &cdesactivado");
		}else {
			return translateColor("&bEstado actual: &aactivado");
		}
	}
	
	public static ItemStack checkItemWLStatusEN() {
		if(SourceManager.getInstance().isWLStatusEnabled()==true) {
			return createItemNoLore(Material.GREEN_CONCRETE, 0, "&aCurrent status: &benabled");
		}else {
			return createItemNoLore(Material.RED_CONCRETE, 0, "&aCurrent status: &cdisabled");
		}
	}
	
	public static ItemStack checkItemWLStatusES() {
		if(SourceManager.getInstance().isWLStatusEnabled()==true) {
			return createItemNoLore(Material.GREEN_CONCRETE, 0, "&aEstado actual: &bactivado");
		}else {
			return createItemNoLore(Material.RED_CONCRETE, 0, "&aCurrent status: &cdesactivado");
		}
	}

	
	@SuppressWarnings("deprecation")
	public static ItemStack createItemHead(String owner,String name,List<String> lore) {
		ItemStack is=new ItemStack(Material.LEGACY_SKULL_ITEM,1,(short)3);
		ItemMeta im=is.getItemMeta();
		if(MainClass.getInstance().getServer().getOnlineMode()==true) {
			SkullMeta sm=(SkullMeta)im;
			sm.setOwner(owner);
			im.setDisplayName(translateColor(name));
			im.setLore(lore);
			is.setItemMeta(im);
		}else {
			im.setDisplayName(translateColor(name));
			im.setLore(lore);
			is.setItemMeta(im);
		}
		return is;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem(Material material, int data,String name,List<String> lore) {
		ItemStack is=new ItemStack(material,1,(short)data);
		ItemMeta im=is.getItemMeta();
		im.setDisplayName(translateColor(name));
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItemNoLore(Material material, int data,String name) {
		ItemStack is=new ItemStack(material,1,(short)data);
		ItemMeta im=is.getItemMeta();
		im.setDisplayName(translateColor(name));
		is.setItemMeta(im);
		return is;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItemNoAttributesNoLore(int option,Material material, int data,String name,List<String> lore) {
		ItemStack is=new ItemStack(material,1,(short)data);
		ItemMeta im=is.getItemMeta();
		im.setDisplayName(translateColor(name));
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		if(option==1) {
			im.setLore(lore);
			is.setItemMeta(im);
		}else if(option==2) {
			is.setItemMeta(im);
		}
		return is;
	}
	
	public static void sendMessageError(Player player) {
		if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
			player.sendMessage(translateColor(MainClass.getInstance().getConfig().getString("Game.message.format-nopermission-msg.english")));
		}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
			player.sendMessage(translateColor(MainClass.getInstance().getConfig().getString("Game.message.format-nopermission-msg.spanish")));
		}
	}
	
	public static void sendSound(Player player,Sound sound) {
		player.playSound(player.getLocation(), sound, 10.0F, 0.0F);
	}
	
	public static void sendMessageToAll(String msg) {
		Bukkit.getServer().broadcastMessage(translateColor(msg));
	}
	
	public static String translateColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static List<String> translateColorArray(List<String> msg){
		List<String> list=new ArrayList<>();
		for(String st:list) {
			msg.add(translateColor(st));
		}
		return list;
	}
	
	
	public static Field pathfinderGoalSelector_GoalSet = null;
	public static boolean good_PathfinderGoalSelector_GoalSet = false;
	  
	public static boolean init(){
	    try{
	    	pathfinderGoalSelector_GoalSet = PathfinderGoalSelector.class.getDeclaredField("b");
	    	pathfinderGoalSelector_GoalSet.setAccessible(true);
	    	good_PathfinderGoalSelector_GoalSet = true;
	    }catch (Exception e1){
	    	try{
	    		pathfinderGoalSelector_GoalSet = PathfinderGoalSelector.class.getDeclaredField("field_75782_a");
	    		pathfinderGoalSelector_GoalSet.setAccessible(true);
	    		good_PathfinderGoalSelector_GoalSet = true;
    		}catch (Exception e2){
    			
    		}
	    }
	    return true;
	}

}
