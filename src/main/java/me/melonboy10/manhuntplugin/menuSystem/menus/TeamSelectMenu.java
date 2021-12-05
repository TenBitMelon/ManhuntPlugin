package me.melonboy10.manhuntplugin.menuSystem.menus;

import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.menuSystem.Menu;
import me.melonboy10.manhuntplugin.utils.PlayerListCollector;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamSelectMenu extends Menu {

    private final ManhuntGame game;

    public TeamSelectMenu(ManhuntGame manhuntGame) {
        this.game = manhuntGame;
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
        Player player = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 11 -> {
                game.setTeam(player, ManhuntGame.Team.RUNNER);
                game.teleportIntoGame(player);
            }
            case 13 -> {
                game.setTeam(player, ManhuntGame.Team.HUNTER);
                game.teleportIntoGame(player);
            }
            case 15 -> {
                game.setTeam(player, ManhuntGame.Team.SPECTATOR);
                game.teleportIntoGame(player);
            }
            case 22 -> {
                player.closeInventory();
                new GamesListMenu().open(player);
            }
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(11, makeItem(Material.CLOCK, ChatColor.YELLOW + "Runners",
            ChatColor.DARK_GRAY + "Players",
            "",
            ChatColor.AQUA + "Players on Team:",
            game.getPlayers().keySet().stream().filter(player -> game.getPlayers().get(player).equals(ManhuntGame.Team.RUNNER)).collect(new PlayerListCollector()),
            "",
            ChatColor.YELLOW + "Click to join!"
        ));

        inventory.setItem(13, makeItem(Material.COMPASS, ChatColor.YELLOW + "Hunters",
            ChatColor.DARK_GRAY + "Players",
            "",
            ChatColor.AQUA + "Players on Team:",
            game.getPlayers().keySet().stream().filter(player -> game.getPlayers().get(player).equals(ManhuntGame.Team.HUNTER)).collect(new PlayerListCollector()),
            "",
            ChatColor.YELLOW + "Click to join!"
        ));

        inventory.setItem(15, makeItem(Material.SPYGLASS, ChatColor.YELLOW + "Spectators",
            ChatColor.DARK_GRAY + "Players",
            "",
            ChatColor.AQUA + "Players on Team:",
            game.getPlayers().keySet().stream().filter(player -> game.getPlayers().get(player).equals(ManhuntGame.Team.SPECTATOR)).collect(new PlayerListCollector()),
            "",
            ChatColor.YELLOW + "Click to join!"
        ));

        inventory.setItem(22, makeItem(Material.ARROW, ChatColor.YELLOW + "Back"));

        setFillerGlass();
    }
}
