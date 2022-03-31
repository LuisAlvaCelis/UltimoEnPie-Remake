package net.ultimoenpie.entitys;

import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_16_R1.ChatMessage;
import net.minecraft.server.v1_16_R1.EntityWolf;
import net.minecraft.server.v1_16_R1.EntityZombie;
import net.minecraft.server.v1_16_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R1.World;

public class CustomZombie extends EntityZombie{

	public CustomZombie(World world) {
		super(world);
		this.setCustomName(new ChatMessage("§cTesting"));
		this.setCustomNameVisible(true);
		this.goalSelector.a(0, new PathfinderGoalNearestAttackableTarget<EntityWolf>(this, EntityWolf.class, true));
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

}
