package com.zluck.warrior.managers.location;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.google.gson.Gson;
import com.zluck.warrior.Warrior;

public class LocationManager {

	public enum LocationType {
		LOBBY, SPAWN, SAIDA, CAMAROTE;

		public static List<String> getLocationsType() {
			List<String> locationsType = new ArrayList<>();

			for (int i = 0; i < values().length; ++i) 
				locationsType.add(values()[i].toString());

			return locationsType;
		}

		@Override
		public String toString() {
			String toString = super.toString();
			
			return toString.substring(0, 1).toUpperCase() + toString.substring(1).toLowerCase();
		}
	}
	
	/**
	 * 
	 * 
	 * @param locationType The {@link LocationType} to return.
	 * 
	 * @return {@link Location} defined in the config.
	 * 
	 * @throws NullPointerException If locationType is null.
	 * @throws IllegalStateException If {@link LocationType} has not been defined in the config.
	 */
	public static Location getLocation(LocationType locationType) {
		if (locationType == null)
			throw new NullPointerException();

		String json = Warrior.getInstance().getConfig().getString("Locais." + locationType.toString());

		if (json == null)
			throw new IllegalStateException("O local " + locationType.toString().toLowerCase() + " nao foi setado.", new NullPointerException());

		LocationConstructor location = new Gson().fromJson(json, LocationConstructor.class);

		return location.getLocation();
	}

	public static void setLocation(LocationType locationType, LocationConstructor location) {
		if (locationType == null || location == null) 
			throw new NullPointerException();

		Warrior.getInstance().getConfig().set("Locais." + locationType.toString(), new Gson().toJson(location));
		Warrior.getInstance().saveConfig();
	}

}
