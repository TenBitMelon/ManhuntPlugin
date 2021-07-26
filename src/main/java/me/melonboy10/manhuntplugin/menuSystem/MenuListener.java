package me.melonboy10.manhuntplugin.menuSystem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();
        //If the inventoryholder of the inventory clicked on is an instance of Menu

        if (holder instanceof Menu) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }

            // the menu we clicked on
            Menu menu = (Menu) holder;
            //Call the handleMenu object which takes the event and processes it
            menu.clickEvent(e);
        }

    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            menu.closeMenu(e);
        }
    }

}
