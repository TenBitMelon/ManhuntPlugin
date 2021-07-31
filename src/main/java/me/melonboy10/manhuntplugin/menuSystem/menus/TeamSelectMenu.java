package me.melonboy10.manhuntplugin.menuSystem.menus;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.menuSystem.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class TeamSelectMenu extends Menu {

    public TeamSelectMenu(ManhuntPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getMenuName() {
        return "Join team!";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void clickEvent(InventoryClickEvent event) {

    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {

        inventory.setItem(11, makeItem(Material.CLOCK, ChatColor.YELLOW + "Runners",
                ChatColor.DARK_GRAY + "Players",
                "",
                ChatColor.AQUA + "Players on Team:",
                ""
            ));

        inventory.setItem(13, makeItem(Material.COMPASS, ChatColor.YELLOW + "Hunters"));

        inventory.setItem(15, makeItem(Material.SPYGLASS, ChatColor.YELLOW + "Spectators"));

        setFillerGlass();
    }
}
