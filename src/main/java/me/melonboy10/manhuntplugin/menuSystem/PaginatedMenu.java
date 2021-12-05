package me.melonboy10.manhuntplugin.menuSystem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected final int maxItemsPerPage = 28;

    public PaginatedMenu() {
        super();
    }

    @Override
    public void clickEvent(InventoryClickEvent event) {
        switch (event.getSlot()) {
            case 49 -> event.getWhoClicked().closeInventory();
            case 48 -> {
                if (page != 0) page--;
                setMenuItems();
            }
            case 50 -> {
                page++;
                setMenuItems();
            }
        }
    }

    //Set the border and menu buttons for the menu
    public void addMenuBorder(boolean close) {
        if (page != 0) {
            inventory.setItem(48, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"));
        } else {
            inventory.setItem(48, fillerGlass);
        }

        if (close) {
            inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.RED + "Close"));
        } else {
            inventory.setItem(49, makeItem(Material.ARROW, ChatColor.GREEN + "Go Back"));
        }

        inventory.setItem(50, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"));

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.fillerGlass);
            }
        }

        inventory.setItem(17, super.fillerGlass);
        inventory.setItem(18, super.fillerGlass);
        inventory.setItem(26, super.fillerGlass);
        inventory.setItem(27, super.fillerGlass);
        inventory.setItem(35, super.fillerGlass);
        inventory.setItem(36, super.fillerGlass);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.fillerGlass);
            }
        }
    }
}