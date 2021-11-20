package me.melonboy10.manhuntplugin.listeners;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getEntity().getWorld().equals(ManhuntPlugin.hubWorld)) {
            ManhuntGame game = ManhuntGameManager.getGame(event.getEntity());
            if (game != null) {
                game.playerDie(event.getEntity());
                event.setCancelled(true);
                game.broadcastMessage(event.getDeathMessage());
                event.getDrops().forEach(itemStack -> event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), itemStack));
            }
        }
    }

}
