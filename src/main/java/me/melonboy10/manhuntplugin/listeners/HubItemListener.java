package me.melonboy10.manhuntplugin.listeners;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.menuSystem.menus.CreateGameMenu;
import me.melonboy10.manhuntplugin.menuSystem.menus.GameHistoryMenu;
import me.melonboy10.manhuntplugin.menuSystem.menus.GamesListMenu;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class HubItemListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getWorld().equals(ManhuntPlugin.hubWorld)) {
            if (event.getItem() != null) {
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                        event.setCancelled(true);
                    }
                    openMenu(event.getItem(), event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getPlayer().getWorld().equals(ManhuntPlugin.hubWorld)) {
            if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);
            }
            openMenu(event.getItemDrop().getItemStack(), event.getPlayer());
        }
    }

    @EventHandler
    public void onMoveItem(InventoryClickEvent event) {
        if (event.getWhoClicked().getWorld().equals(ManhuntPlugin.hubWorld)) {
            if (event.getCurrentItem() != null) {
                if (!event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
                    event.setCancelled(true);
                }
                openMenu(event.getCurrentItem(), (Player) event.getWhoClicked());
            }
        }
    }

    private void openMenu(ItemStack item, Player player) {
        if (player.getWorld().equals(ManhuntPlugin.hubWorld)) {
            switch (item.getType()) {
                case COMPASS -> new GamesListMenu().open(player);
                case WRITTEN_BOOK -> new GameHistoryMenu().open(player);
                case GOLDEN_SWORD -> new CreateGameMenu(" ", player).open(player);
            }
        }
    }

}
