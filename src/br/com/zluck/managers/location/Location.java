package br.com.zluck.managers.location;

import org.bukkit.Bukkit;

public class Location {

	private String worldName;
	private double x, y, z;
	private float yaw, pitch;

	public Location(String worldName, double x, double y, double z, float yaw, float pitch) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Location(org.bukkit.Location location) {
		this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(),
				location.getPitch());
	}

	public String getWorldName() {
		return worldName;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public org.bukkit.Location getLocation() {
		return new org.bukkit.Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
	}
}
