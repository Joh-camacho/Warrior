package br.com.zluck.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class Utils {

	public static void clearPlayer(Player player) {
		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setItemOnCursor(null);
		player.closeInventory();
		player.setFireTicks(0);
		player.setHealth(player.getMaxHealth());
		player.setAllowFlight(false);
		player.setFlying(false);
	}

	public static boolean invNotEmpty(Player player) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR) {
				return true;
			}
		}
		for (ItemStack item : player.getInventory().getArmorContents()) {
			if (item != null && item.getType() != Material.AIR) {
				return true;
			}
		}

		return false;
	}

	public static void sendActionBar(Player player, String text) {
		PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static String contentsToBase64(ItemStack[] contents) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeObject(contents);
			dataOutput.close();

			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Could not convert inventory to base64.", e);
		}
	}

	public static ItemStack[] contentsFromBase64(String data) {
		if (data == null) 
			return null;
		
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack[] contents = (ItemStack[]) dataInput.readObject();
			dataInput.close();

			return contents;
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Unable to decode the class type.", ex);
		} catch (IOException ex) {
			throw new RuntimeException("Unable to convert Inventory to Base64.", ex);
		}
	}

	public static void spawnFirework(Location location) {
		Firework firework = location.getWorld().spawn(location, Firework.class);
		FireworkMeta fireworkMeta = firework.getFireworkMeta();
		fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(Type.BALL)
				.withColor(Color.GREEN).withFade(Color.RED).build());
		fireworkMeta.setPower(0);
		firework.setFireworkMeta(fireworkMeta);
	}

	public static String getTime(int time) {
		if (time == 0) 
			return "00:00";

		int minutes = time / 60;
		int seconds = time % 60;
		String minutesFormat = (minutes < 10) ? "0" : "" + minutes;
		String secondsFormat = (seconds < 10) ? "0" : "" + seconds;

		return minutesFormat + ":" + secondsFormat;
	}

}
