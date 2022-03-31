package net.ultimoenpie.entitys;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.ultimoenpie.main.MainClass;

public class SpawnCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		if(!cmd.getName().equals("spawn")) {
			return false;
		}else {
			if(args[0].equalsIgnoreCase("zombie")) {
				new BukkitRunnable() {
					
					@Override
					public void run() {
						Collection<? extends Player> onlinePlayers;
						int random =new Random().nextInt((onlinePlayers = Bukkit.getOnlinePlayers()).size());
						Player player=(Player)onlinePlayers.toArray()[random];
						Location loc=player.getLocation();
						CustomZombie cz=new CustomZombie(((CraftWorld)loc.getWorld()).getHandle());
						cz.setSpawnLocation(loc);
						sender.sendMessage("§aZombie has been spawned in: "+loc.getX()+", "+loc.getY()+", "+loc.getZ());
					}
				}.runTaskTimer(MainClass.getInstance(), 300L,300L);
			}else if(args[0].equalsIgnoreCase("bat")) {
				new BukkitRunnable() {
					
					@Override
					public void run() {
						Collection<? extends Player> onlinePlayers;
						int random =new Random().nextInt((onlinePlayers = Bukkit.getOnlinePlayers()).size());
						Player player=(Player)onlinePlayers.toArray()[random];
						Location loc=player.getLocation();
						CustomBAT cbat=new CustomBAT(((CraftWorld)loc.getWorld()).getHandle());
						cbat.setSpawnLocation(loc);
						sender.sendMessage("§aBat has been spawned in: "+loc.getX()+", "+loc.getY()+", "+loc.getZ());
					}
				}.runTaskTimer(MainClass.getInstance(), 300L,300L);
			}
		}
		return false;
	}
	/*Collection<? extends Player> onlinePlayers;
	int random =new Random().nextInt((onlinePlayers = Bukkit.getOnlinePlayers()).size());
	Player player=(Player)onlinePlayers.toArray()[random];*/
}
