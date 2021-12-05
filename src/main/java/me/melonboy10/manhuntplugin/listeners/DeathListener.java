package me.melonboy10.manhuntplugin.listeners;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getEntity().getWorld().equals(ManhuntPlugin.hubWorld)) {
            ManhuntGame game = ManhuntGameManager.getGame(event.getEntity());
            if (game != null) {
                event.getDrops().removeIf(itemStack -> itemStack.getType().equals(Material.COMPASS));
                Location deathLocation = event.getEntity().getLocation();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getEntity().spigot().respawn();
                        game.playerDie(event.getEntity());
                        if (game.getTeam(event.getEntity()).equals(ManhuntGame.Team.RUNNER)) {
                            event.getEntity().teleport(deathLocation);
                        }
                    }
                }.runTaskLater(ManhuntPlugin.plugin, 1);
            }
        }
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
            Player player = event.getEntity().getKiller();
            ManhuntGame game = ManhuntGameManager.getGame(player);
            if (game != null) {
                game.checkWinConditions();
            } else {
                assert player != null;
                player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "How did you just kill an Ender Dragon!?!");
            }
        }
    }

}
