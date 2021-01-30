package br.com.zluck.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import br.com.zluck.Warrior;

public class ChatListener implements Listener {

	@EventHandler
	private void onChat(ChatMessageEvent event) {
		String winner = Warrior.getInstance().getConfig().getString("Winner");
		String tag = Warrior.getInstance().getConfig().getString("TagChat");

		if (winner == null)
			return;

		if (event.getTags().contains("warrior") && winner.equalsIgnoreCase(event.getSender().getName())) {
			event.setTagValue("warrior", tag);
		}
	}

}
