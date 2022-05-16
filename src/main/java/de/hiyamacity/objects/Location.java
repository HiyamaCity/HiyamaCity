package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
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

    public org.bukkit.Location getAsBukkitLocation() {
        return new org.bukkit.Location(Bukkit.getWorld((this.getWorld() == null) ? "world" : this.getWorld()), this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }

}
