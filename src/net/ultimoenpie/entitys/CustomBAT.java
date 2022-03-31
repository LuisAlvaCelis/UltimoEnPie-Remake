package net.ultimoenpie.entitys;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftBat;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_16_R1.ChatMessage;
import net.minecraft.server.v1_16_R1.EntityBat;
import net.minecraft.server.v1_16_R1.EntityHuman;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R1.World;
import net.ultimoenpie.main.MainClass;
import net.ultimoenpie.utils.SourceCode;

public class CustomBAT extends EntityBat implements Listener{

	public CustomBAT(World world) {
		super(EntityTypes.BAT, world);
		Bukkit.getPluginManager().registerEvents(this, MainClass.getInstance());
		this.modiftyBehavior();
	}
	
	private void modiftyBehavior() {
		this.setCustomName(new ChatMessage(SourceCode.translateColor("&aBat &c["+getMaxHealth()+"]")));
		this.setCustomNameVisible(true);
		this.setAggressive(true);
		this.initPathfinder();
	}
	
	@Override
	public void initPathfinder() {
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
	}
	
	public static Object getPrivateField(String fieldName, Class clazz,Object object) {
        Field field;
        Object o = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
	
	public void setSpawnLocation(Location loc) {
		double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float newYaw = loc.getYaw();
        float newPitch = loc.getPitch();
        setLocation(x, y, z, newYaw, newPitch);
        this.world.addEntity(this,SpawnReason.CUSTOM);
	}
	
	@EventHandler
	public void onEntityDamgeByEntity(EntityDamageByEntityEvent e) {
		Entity entity=e.getEntity();
		Entity damager=e.getDamager();
		if((entity instanceof CraftBat) && (damager instanceof Player)) {
			Bat victim=(Bat)entity;
			entity.setCustomName("§aBat §c["+victim.getHealth()+"]");
			entity.setCustomNameVisible(true);
		}
	}
}
