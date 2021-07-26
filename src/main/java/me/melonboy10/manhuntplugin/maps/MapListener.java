package me.melonboy10.manhuntplugin.maps;

import me.melonboy10.manhuntplugin.ManhuntGame;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class MapListener implements Listener {

    private ManhuntPlugin plugin;
    private static ArrayList<Player> playersWithItem = new ArrayList<>();

    public MapListener(ManhuntPlugin plugin) {
        this.plugin = plugin;
    }

    public static void addPlayer(Player player) {
        playersWithItem.add(player);
    }

    private boolean checkItem(ItemStack itemStack, Player player) {
        if (!ManhuntGame.inGame) {
            if (itemStack != null && player != null) {
                if (itemStack.getType().equals(Material.FILLED_MAP)) {
                    ItemMeta meta = itemStack.getItemMeta();
                    if (meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "viewing-map"), PersistentDataType.INTEGER)) {
                        player.getInventory().remove(Material.FILLED_MAP);
                        playersWithItem.remove(player);
                        ManhuntGame.creationMenu.open(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (checkItem(event.getItem(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (checkItem(event.getItemDrop().getItemStack(), event.getPlayer())) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onSwitch(PlayerItemHeldEvent event) {
        if (playersWithItem.contains(event.getPlayer())) {
            event.getPlayer().getInventory().remove(Material.FILLED_MAP);
            playersWithItem.remove(event.getPlayer());
            ManhuntGame.creationMenu.open(event.getPlayer());
        }
    }

    @EventHandler
    public void onMoveItem(InventoryClickEvent event) {
        if (checkItem(event.getCurrentItem(), (Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }
}
