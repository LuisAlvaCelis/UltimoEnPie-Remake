package net.ultimoenpie.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.ultimoenpie.main.MainClass;
import net.ultimoenpie.utils.SourceCode;
import net.ultimoenpie.utils.SourceManager;

public class Listeners implements Listener{

	private MainClass plugin;
	
	public Listeners(MainClass pl) {
		this.plugin=pl;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player player=e.getPlayer();		
		if(SourceManager.getInstance().isWLStatusEnabled()==true && !SourceManager.getInstance().getListPlayersWL().contains(player.getUniqueId().toString())){
			if(!player.hasPermission("ultimoenpie.staff") && !player.hasPermission("ultimoenpie.spec") && !player.isOp()) {
				if(!SourceManager.getInstance().getListPlayers().contains(player.getUniqueId().toString())) {
					SourceManager.getInstance().addPlayerToListPlayers(player,"EN");
					e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, SourceManager.getInstance().getKickMSG(SourceManager.getInstance().getLanguagePlayer(player)));
				}else {
					e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, SourceManager.getInstance().getKickMSG(SourceManager.getInstance().getLanguagePlayer(player)));
				}
			}else {
				if(!SourceManager.getInstance().getListPlayers().contains(player.getUniqueId().toString())) {
					SourceManager.getInstance().addPlayerToListPlayers(player,"EN");
					e.allow();
				}else {
					e.allow();
				}
			}
		}else {
			e.allow();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Player player=e.getPlayer();
		SourceManager.getInstance().sendMessageJoin(player);
		SourceManager.getInstance().sendMessageJoinToAll(player);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player player=e.getPlayer();
		SourceManager.getInstance().sendMessageQuitToAll(player);
	}
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		SourceCode.sendMessageToAll(plugin.getConfig().getString("Game.message.format-msg").replace("{player}", e.getPlayer().getName()).replace("{message}", e.getMessage()));
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player player=(Player)e.getWhoClicked();
		ItemStack is=e.getCurrentItem();
		if(is==null || is.getItemMeta()==null || is.getItemMeta().getDisplayName()==null || is.getType()==null) {
			return;
		}else {
			InventoryView iv=e.getView();
			if(iv.getTitle().equalsIgnoreCase("§cLanguage / Idioma")) {
				e.setCancelled(true);
				if(e.getRawSlot()==3) {
					if(!SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
						SourceManager.getInstance().setLanguagePlayer(player, "EN");
						SourceCode.openGUI(player, 1);
						player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" &b[Language] You language is now English"));
						SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
					}else {
						player.sendMessage(SourceCode.translateColor("&cError: Language has already been set to English previously"));
						SourceCode.sendSound(player, Sound.BLOCK_ANVIL_PLACE);
					}
				}else if(e.getRawSlot()==5) {
					if(!SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
						SourceManager.getInstance().setLanguagePlayer(player, "ES");
						SourceCode.openGUI(player, 1);
						player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" &b[Language] Tu idioma ahora es Español"));
						SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
					}else {
						player.sendMessage(SourceCode.translateColor("&cError: El idioma ya se ha establecido en Español anteriormente"));
						SourceCode.sendSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
						SourceCode.sendSound(player, Sound.BLOCK_ANVIL_PLACE);
					}
				}
			}else if(iv.getTitle().equalsIgnoreCase("§c§lLast One Standing") || iv.getTitle().equalsIgnoreCase("§c§lUltimo En Pie")) {
				e.setCancelled(true);
				if(e.getRawSlot()==0) {
					if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
						if(SourceManager.getInstance().isPVPStatus()==false) {
							SourceManager.getInstance().setPVPStatus(true);
							for(int i=0;i<plugin.getServer().getWorlds().size();i++) {
								this.plugin.getServer().getWorlds().get(i).setPVP(true);
							}
							player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" PvP has been &2enabled."));
							SourceCode.openGUI(player, 2);
							SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
						}else {
							SourceManager.getInstance().setPVPStatus(false);
							for(int i=0;i<plugin.getServer().getWorlds().size();i++) {
								this.plugin.getServer().getWorlds().get(i).setPVP(false);
							}
							player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" PvP has been &cdisabled."));
							SourceCode.openGUI(player, 2);
							SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
						}
						
					}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
						if(SourceManager.getInstance().isPVPStatus()==false) {
							SourceManager.getInstance().setPVPStatus(true);
							for(int i=0;i<plugin.getServer().getWorlds().size();i++) {
								this.plugin.getServer().getWorlds().get(i).setPVP(true);
							}
							player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" PvP ha sido &2habilitado."));
							SourceCode.openGUI(player, 2);
							SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
						}else {
							SourceManager.getInstance().setPVPStatus(false);
							for(int i=0;i<plugin.getServer().getWorlds().size();i++) {
								this.plugin.getServer().getWorlds().get(i).setPVP(true);
							}
							player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" PvP ha sido &cdesactivado."));
							SourceCode.openGUI(player, 2);
							SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
						}
					}
				}else if(e.getRawSlot()==1) {
					SourceCode.openGUI(player, 3);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==2) {
					SourceCode.openGUI(player, 6);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}
			}else if(iv.getTitle().equalsIgnoreCase("§3§lWhitelist") || iv.getTitle().equalsIgnoreCase("§3§lLista blanca")) {
				e.setCancelled(true);
				if(e.getRawSlot()==0) {
					SourceCode.openGUI(player, 4);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==1) {
					SourceCode.openGUI(player, 5);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==2) {
					player.chat("/ultimoenpie wl toggle");
					SourceCode.openGUI(player, 3);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==3) {
					SourceCode.openGUI(player, 2);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==53) {
					if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
						player.sendMessage(SourceCode.translateColor("&cError: Coming Soon"));
						SourceCode.sendSound(player, Sound.BLOCK_ANVIL_FALL);
					}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
						player.sendMessage(SourceCode.translateColor("&cError: Próximamente"));
						SourceCode.sendSound(player, Sound.BLOCK_ANVIL_FALL);
					}
				}
			}else if(iv.getTitle().equalsIgnoreCase("§8§lSelect mob type") || iv.getTitle().equalsIgnoreCase("§8§lSeleccione tipo de mob")) {
				e.setCancelled(true);
				if(e.getRawSlot()==1) {
					SourceCode.openGUI(player, 7);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==3) {
					SourceCode.openGUI(player, 8);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==5) {
					SourceCode.openGUI(player, 9);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==7) {
					SourceCode.openGUI(player, 10);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else if(e.getRawSlot()==13) {
					SourceCode.openGUI(player, 2);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}
			}else if(iv.getTitle().equalsIgnoreCase("§3§lPassive mobs") || iv.getTitle().equalsIgnoreCase("§3§lMobs pasivos")) {
				e.setCancelled(true);
				if(e.getRawSlot()==53) {
					SourceCode.openGUI(player, 6);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else {
					for(int i=0;i<SourceManager.getInstance().getPassiveMobs().size();i++) {
						if(e.getRawSlot()==i) {
							SourceCode.openGUIMob(player, e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
							SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
						}
					}
				}
			}else if(iv.getTitle().equalsIgnoreCase("§3§lNeutral mobs") || iv.getTitle().equalsIgnoreCase("§3§lMobs neutrales")) {
				e.setCancelled(true);
				if(e.getRawSlot()==22) {
					SourceCode.openGUI(player, 6);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}
			}else if(iv.getTitle().equalsIgnoreCase("§3§lHostile mobs") || iv.getTitle().equalsIgnoreCase("§3§lMobs hostiles")) {
				e.setCancelled(true);
				if(e.getRawSlot()==35) {
					SourceCode.openGUI(player, 6);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}
			}else if(iv.getTitle().equalsIgnoreCase("§3§lBoss mobs") || iv.getTitle().equalsIgnoreCase("§3§lMobs jefes")) {
				e.setCancelled(true);
				if(e.getRawSlot()==8) {
					SourceCode.openGUI(player, 6);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}
			}else {
				for(int i=0;i<SourceManager.getInstance().getPassiveMobs().size();i++) {
					if(iv.getTitle().equalsIgnoreCase("§2"+SourceManager.getInstance().getPassiveMobs().get(i))) {
						e.setCancelled(true);
						if(e.getRawSlot()==0) {
							SourceManager.getInstance().toggleAggressiveness("BAT");
							if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
								if(MainClass.getInstance().getConfig().getBoolean("Game.mobs.stats.BAT.aggressiveness")==false) {
									player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" &eBat current behavior: &bpacific"));
									SourceCode.openGUIMob(player, "BAT");
									SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
								}else {
									player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" &eBat current behavior: &baggressive"));
									SourceCode.openGUIMob(player, "BAT");
									SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
								}
							}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
								if(MainClass.getInstance().getConfig().getBoolean("Game.mobs.stats.BAT.aggressiveness")==false) {
									player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" &eComportamiento actual del murciélago: &bpacífico"));
									SourceCode.openGUIMob(player, "BAT");
									SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
								}else {
									player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" &eComportamiento actual del murciélago: &bagresivo"));
									SourceCode.openGUIMob(player, "BAT");
									SourceCode.sendSound(player, Sound.BLOCK_LEVER_CLICK);
								}
							}
						}else if(e.getRawSlot()==17) {
							SourceCode.openGUI(player, 7);
							SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
						}
					}
				}
			}
		}
		
	}
	
	
	/*@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		Entity entity=e.getEntity();
		if(entity instanceof LivingEntity) {
			LivingEntity le=(LivingEntity)entity;				
			if(le.getType()==EntityType.BAT) {
				try {
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}*/
}
