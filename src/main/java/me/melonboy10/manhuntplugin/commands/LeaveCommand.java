package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand {

    @Command(name = "", aliases = {"l"}, desc = "Leave the manhunt game.")
    public void root(@Sender CommandSender sender) {
        if (sender instanceof Player) {
            if (ManhuntGameManager.isPlayerInGame(((Player) sender))) {
                ManhuntGameManager.getGame((Player) sender).playerLeave((Player) sender);
            }
        }
    }

}
