package me.melonboy10.manhuntplugin.listeners;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class WorldLoadListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        ManhuntGame game = ManhuntGameManager.getGame(Integer.parseInt(event.getWorld().getName().split("-")[0]));
        if (game != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    game.getTeamTextMenu().update();
                }
            }.runTaskLater(ManhuntPlugin.plugin, 10);
        }
    }
}
