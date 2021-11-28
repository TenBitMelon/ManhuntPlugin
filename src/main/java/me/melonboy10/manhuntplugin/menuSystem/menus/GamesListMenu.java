package me.melonboy10.manhuntplugin.menuSystem.menus;

import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.menuSystem.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.List;

public class GamesListMenu extends PaginatedMenu {

    @Override
    public String getMenuName() {
        return "Running Games";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void clickEvent(InventoryClickEvent event) {
        if(event.getSlot() == 49) {
            event.getWhoClicked().closeInventory();
        }
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        addMenuBorder(true);
        List<ManhuntGame> games = ManhuntGameManager.getGames();
        for (int i = 0; i < games.size() && i < maxItemsPerPage; i++) {
            ManhuntGame game = games.get(i);
            inventory.addItem(makeItem(switch (game.getState()) {
                case GENERATING -> Material.STONE;
                case HUNTER_COOLDOWN -> Material.DIRT;
                case PLAYING -> Material.GRASS_BLOCK;
                case GAME_OVER -> Material.BARRIER;
            }, ChatColor.YELLOW + "Game #" + game.hashCode(),
                ChatColor.DARK_GRAY + "Manhunt Game",
                "",
                ChatColor.AQUA + "Game State:",
                ChatColor.GRAY + "" + game.getState(),
                "",
                ChatColor.AQUA + "Players: ",
                ChatColor.GRAY + "" + game.getPlayers().size(),
                "",
                ChatColor.AQUA + "World Type: ",
                ChatColor.GRAY + "" + game.getSettings().getWorldType(),
                "",
                ChatColor.AQUA + "Privacy: ",
                ChatColor.GRAY + "" + game.getSettings().getPrivacy(),
                "",
                ChatColor.YELLOW + "Click to join!"
            ));
        }
    }

}
