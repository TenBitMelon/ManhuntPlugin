package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.LastArg;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.ManhuntGameOld;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.menuSystem.menus.CreateGameMenu;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateGameCommand {

    private static ManhuntPlugin plugin;

    public CreateGameCommand(ManhuntPlugin manhuntPlugin) {
        plugin = manhuntPlugin;
    }

    @Command(name = "", aliases = {"creategame, game"}, desc = "Create a new game")
    public void root(@Sender CommandSender sender, @OptArg(" ") String seed) {
        if (sender instanceof Player || sender instanceof BlockCommandSender) {
            new CreateGameMenu(plugin, seed);
            ManhuntGameOld.createGame(sender);
        }
    }

}
