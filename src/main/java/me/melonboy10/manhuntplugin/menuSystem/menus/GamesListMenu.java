package me.melonboy10.manhuntplugin.menuSystem.menus;

import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.game.ManhuntGameSettings;
import me.melonboy10.manhuntplugin.menuSystem.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        super.clickEvent(event);
        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "game-id"), PersistentDataType.INTEGER)) {
            int gameId = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "game-id"), PersistentDataType.INTEGER);
            ((Player) event.getWhoClicked()).performCommand("join " + gameId);
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder(true);

        //show game
        //don't show if private
        //don't show if over
        //sort by public then spec only
        //sort by game time

        List<ManhuntGame> games = ManhuntGameManager.getGames().stream()
            .filter(manhuntGame -> !manhuntGame.getSettings().getPrivacy().equals(ManhuntGameSettings.Privacy.PRIVATE))
            .filter(manhuntGame -> !manhuntGame.getState().equals(ManhuntGame.GameState.GAME_OVER))
            .sorted(Comparator.comparing(game -> game.getSettings().getPrivacy()))
            .collect(Collectors.toList());

        for (int i = 0; i < games.size() && i < maxItemsPerPage; i++) {
            ManhuntGame game = games.get(i);
            inventory.addItem(makeItem(
                (game.getSettings().getPrivacy().equals(ManhuntGameSettings.Privacy.PUBLIC) ?
                    switch (game.getState()) {
                        case GENERATING -> Material.STONE;
                        case HUNTER_COOLDOWN -> Material.DIRT;
                        case PLAYING -> Material.GRASS_BLOCK;
                        case GAME_OVER -> Material.BARRIER;
                    } :
                    switch (game.getState()) {
                        case GENERATING -> Material.FIRE_CHARGE;
                        case HUNTER_COOLDOWN -> Material.SLIME_BALL;
                        case PLAYING -> Material.ENDER_EYE;
                        case GAME_OVER -> Material.BARRIER;
                    }
                ),
                ChatColor.YELLOW + "Game #" + game.hashCode(),
                1, "game-id", game.hashCode(),
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
