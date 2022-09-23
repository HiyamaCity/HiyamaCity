package de.hiyamacity.objects;

import de.hiyamacity.util.JsonHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

	private String world;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public Location(org.bukkit.Location loc) {
		this.world = loc.getWorld().getName();
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.yaw = loc.getYaw();
		this.pitch = loc.getPitch();
	}

	public Location(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static org.bukkit.Location getAsBukkitLocation(Location loc) {
		return new org.bukkit.Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	@Override
	public String toString() {
		return JsonHandler.getObjectAsJson(this);
	}

}
