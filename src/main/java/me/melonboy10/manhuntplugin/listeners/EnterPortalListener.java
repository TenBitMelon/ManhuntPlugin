package me.melonboy10.manhuntplugin.listeners;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class EnterPortalListener implements Listener {

    //TODO: Move this to the ManhuntGameWorldManager
    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        World world = event.getEntity().getWorld();
        if (!world.getName().contains("hub")) {
            ManhuntGame game = ManhuntGameManager.getGame(Integer.parseInt(world.getName().split("-")[0]));
            event.getTo().setWorld(switch (event.getTo().getWorld().getEnvironment()) {
                case NORMAL -> game.getOverworld();
                case NETHER -> game.getNether();
                case THE_END -> game.getEnd();
                case CUSTOM -> ManhuntPlugin.hubWorld;
            });
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        World world = event.getPlayer().getWorld();
        if (!world.getName().contains("hub")) {
            ManhuntGame game = ManhuntGameManager.getGame(Integer.parseInt(world.getName().split("-")[0]));
            event.getTo().setWorld(switch (event.getTo().getWorld().getEnvironment()) {
                case NORMAL -> game.getOverworld();
                case NETHER -> game.getNether();
                case THE_END -> game.getEnd();
                case CUSTOM -> ManhuntPlugin.hubWorld;
            });
        }
    }
}
