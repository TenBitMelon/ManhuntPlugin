package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.menuSystem.menus.CreateGameMenu;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CreateGameCommand {

    private static final ManhuntPlugin plugin = ManhuntPlugin.plugin;
    public static HashMap<Player, CreateGameMenu> playerMenuMap = new HashMap<>();

    /*
    add another argument that takes in the hash code of the menu to open other menus - no edit though
     */

    @Command(name = "", desc = "Create a new game")
    public void root(@Sender CommandSender sender, @OptArg(" ") String seed) {
        if (sender instanceof Player) {
            if (playerMenuMap.containsKey((Player) sender)) {
                if (playerMenuMap.get(((Player) sender)) != null) {
                    playerMenuMap.get((Player) sender).open((Player) sender);
                } else {
                    MessageUtils.sendError(((Player) sender), "You are already creating a game!");
                }
            } else {
                if (ManhuntGameManager.isPlayerInGame((Player) sender)) {
                    MessageUtils.sendError(((Player) sender), "You are currently in a game. Type /leave to leave the game.");
                } else {
                    CreateGameMenu menu = new CreateGameMenu(seed, (Player) sender);
                    playerMenuMap.put((Player) sender, menu);
                    menu.open((Player) sender);
                }
            }
        }
    }

}
