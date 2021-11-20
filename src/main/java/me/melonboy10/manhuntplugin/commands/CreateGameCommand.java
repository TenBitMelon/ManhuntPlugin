package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.menuSystem.menus.CreateGameMenu;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CreateGameCommand {

    public static HashMap<Player, CreateGameMenu> playerMenuMap = new HashMap<>();

    @Command(name = "", desc = "Create a new game")
    public void root(@Sender Player player, @OptArg(" ") String seed) {
        if (playerMenuMap.containsKey(player)) {
            if (playerMenuMap.get(player) != null) {
                playerMenuMap.get(player).open(player);
            } else {
                MessageUtils.sendError(player, "You are already creating a game!");
            }
        } else {
            if (ManhuntGameManager.isPlayerInGame(player)) {
                MessageUtils.sendError(player, "You are currently in a game. Type /leave to leave the game.");
            } else {
                CreateGameMenu menu = new CreateGameMenu(seed, player);
                playerMenuMap.put(player, menu);
                menu.open(player);
            }
        }
    }

}
