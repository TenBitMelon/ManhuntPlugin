package me.melonboy10.manhuntplugin.listeners;

import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        event.setQuitMessage("");
        if (ManhuntGameManager.isPlayerInGame(event.getPlayer())) {
            ManhuntGameManager.getGame(event.getPlayer()).playerLeave(event.getPlayer());
        }
    }

}
