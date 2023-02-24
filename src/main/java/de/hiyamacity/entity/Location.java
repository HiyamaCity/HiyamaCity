package de.hiyamacity.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hiyamacity.Main;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.logging.Level;

@Entity
@Table(name = "afk_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(name = "world")
	private String world;

	@Column(name = "x", nullable = false)
	private double x;

	@Column(name = "y", nullable = false)
	private double y;

	@Column(name = "z", nullable = false)
	private double z;

	@Column(name = "pitch", nullable = false)
	private float pitch;

	@Column(name = "yaw", nullable = false)
	private float yaw;

	public org.bukkit.Location toBukkitLocation() {
		return new org.bukkit.Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
	}
	
	public Location fromBukkitLocation(org.bukkit.Location location) {
		this.world = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		return this;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			Main.getInstance().getLogger().log(Level.WARNING, "Error writing object as json.");
		}
		return null;
	}
}