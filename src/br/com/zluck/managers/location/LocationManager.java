package br.com.zluck.managers.location;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import br.com.zluck.Warrior;

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
	
	public static org.bukkit.Location getLocation(LocationType locationType) {
		if (locationType == null)
			throw new NullPointerException();

		String json = Warrior.getInstance().getConfig().getString("Locais." + locationType.toString());

		if (json == null)
			throw new NullPointerException("O local " + locationType.toString().toLowerCase() + " nao foi setado.");

		Location location = new Gson().fromJson(json, Location.class);

		return location.getLocation();
	}

	public static void setLocation(LocationType locationType, Location location) {
		if (locationType == null || location == null) 
			throw new NullPointerException();

		Warrior.getInstance().getConfig().set("Locais." + locationType.toString(), new Gson().toJson(location));
		Warrior.getInstance().saveConfig();
	}

}
