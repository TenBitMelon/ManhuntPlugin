package me.melonboy10.manhuntplugin.listeners;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.getRecipients().clear();
        if (event.getPlayer().getWorld().equals(ManhuntPlugin.hubWorld)) {
            event.getRecipients().addAll(ManhuntPlugin.hubWorld.getPlayers());
        } else {
            ManhuntGame game = ManhuntGameManager.getGame(event.getPlayer());
            if (game != null) {
                event.getRecipients().addAll(game.getPlayers().keySet());
            }
        }
    }

}
