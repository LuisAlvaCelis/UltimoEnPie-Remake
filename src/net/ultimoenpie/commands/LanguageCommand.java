package net.ultimoenpie.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.ultimoenpie.utils.SourceCode;
import net.ultimoenpie.utils.SourceManager;

public class LanguageCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		if(!cmd.getName().equalsIgnoreCase("language")) {
			return false;
		}else {
			if(sender instanceof Player) {
				Player player=(Player)sender;
				if(args.length<1) {
					SourceCode.openGUI(player, 1);
					SourceCode.sendSound(player, Sound.BLOCK_CHEST_OPEN);
				}else {
					if(SourceManager.getInstance().getLanguagePlayer(player).equalsIgnoreCase("EN")) {
						player.sendMessage(SourceCode.translateColor("&cError: Usage /language"));
					}else {
						player.sendMessage(SourceCode.translateColor("&cError: Usa /language"));
					}
				}
			}else{
				sender.sendMessage(SourceCode.translateColor("&cError: You don't have permission to execute this command with the console."));
			}
			return true;
		}
	}

}
