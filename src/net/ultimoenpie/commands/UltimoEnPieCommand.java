package net.ultimoenpie.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.ultimoenpie.main.MainClass;
import net.ultimoenpie.utils.SourceCode;
import net.ultimoenpie.utils.SourceManager;

public class UltimoEnPieCommand implements CommandExecutor{
	
	private MainClass plugin;
	
	public UltimoEnPieCommand(MainClass pl) {
		this.plugin=pl;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender,Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("ultimoenpie")) {
			return false;
		}else {
			if(sender instanceof Player) {
				Player player=(Player)sender;
				if(player.hasPermission("ultimoenpie.*") || player.isOp()) {
					if(args.length<1) {
				        SourceCode.openGUI(player, 2);
						SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
					}else {
						
						if(args[0].equalsIgnoreCase("wl")) {
							if(args[1].equalsIgnoreCase("list")) {
								player.sendMessage(SourceCode.translateColor("&aWhitelist list:"));
								for(int i=0;i<SourceManager.getInstance().getListPlayers().size();i++) {
									player.sendMessage(SourceCode.translateColor("&a - "+SourceManager.getInstance().getListPlayers().get(i)));
								}
							}else if(args[1].equalsIgnoreCase("add")) {
								OfflinePlayer target=Bukkit.getServer().getOfflinePlayer(args[2]);
								if(!SourceManager.getInstance().getListPlayersWL().contains(target.getUniqueId().toString())) {
									SourceManager.getInstance().addPlayerToWL(target);
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" "+target.getName()+" has been add to whitelist."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" "+target.getName()+" ha sido agregado a la lista blanca."));
									}
								}else {
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor("&cError: "+target.getName()+" is already whitelisted."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor("&cError: "+target.getName()+" ya está en la lista blanca."));
									}
								}
							}else if(args[1].equalsIgnoreCase("remove")) {
								OfflinePlayer target=Bukkit.getServer().getOfflinePlayer(args[2]);
								if(SourceManager.getInstance().getListPlayersWL().contains(target.getUniqueId().toString())) {
									SourceManager.getInstance().removePlayerFromWL(target);
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" "+target.getName()+" has been remove from whitelist."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" "+target.getName()+" ha sido removido de la lista blanca."));
									}
								}else {
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor("&cError: "+target.getName()+" is not whitelisted."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor("&cError: "+target.getName()+" no está en la lista blanca."));
									}
								}
							}else if(args[1].equalsIgnoreCase("on")) {
								if(SourceManager.getInstance().isWLStatusEnabled()==false) {
									SourceManager.getInstance().setWLStatus(true);
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" Whitelist is now &eenabled."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" La lista blanca ahora está &eactivada."));
									}
								}else {
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor("&cError: Whitelist is already enabled."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor("&cError: La lista blanca ya está activada."));
									}
								}
							}else if(args[1].equalsIgnoreCase("off")) {
								if(SourceManager.getInstance().isWLStatusEnabled()==true) {
									SourceManager.getInstance().setWLStatus(false);
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" Whitelist is now &cdisabled."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" La lista blanca ahora está &cdesactivada."));
									}
								}else {
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor("&cError: Whitelist is already disabled."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor("&cError: La lista blanca ya está desactivada."));
									}
								}
							}else if(args[1].equalsIgnoreCase("toggle")) {
								if(SourceManager.getInstance().isWLStatusEnabled()==true) {
									SourceManager.getInstance().setWLStatus(false);
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" Whitelist is now &cdisabled."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" La lista blanca ahora está &cdesactivada."));
									}
								}else {
									SourceManager.getInstance().setWLStatus(true);
									if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" Whitelist is now &eenabled."));
									}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
										player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" La lista blanca ahora está &eactivada."));
									}
								}
							}else {
								if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
									player.sendMessage(SourceCode.translateColor("&cError: Usage /ultimoenpie <wl> <add,remove,on,off,toggle>"));
								}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
									player.sendMessage(SourceCode.translateColor("&cError: Usa /ultimoenpie <wl> <add,remove,on,off,toggle>"));
								}
							}
						}else if(args[0].equalsIgnoreCase("reload")) {
							this.plugin.reloadConfig();
							if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
								player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" config.yml has been reloaded."));
							}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
								player.sendMessage(SourceCode.translateColor(SourceManager.getInstance().getPrefix()+" config.yml ha sido recargado."));
							}
						}else{
							if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
								player.sendMessage(SourceCode.translateColor("&cError: Usage /ultimoenpie <empty/reload>"));
							}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
								player.sendMessage(SourceCode.translateColor("&cError: Usa /ultimoenpie <nada/reload>"));
							}
						}
					}
				}else {
					if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
						player.sendMessage(SourceManager.getInstance().getMessageNOPermission("EN"));
					}else if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("ES")) {
						player.sendMessage(SourceManager.getInstance().getMessageNOPermission("ES"));
					}
				}
			}else {
				sender.sendMessage(SourceCode.translateColor("&cError: You don't have permission to execute this command with the console."));
			}
			return true;
		}
	}

	
}
