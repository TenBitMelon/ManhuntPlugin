package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand {

    @Command(name = "", desc = "Used to join a game.")
    public void root(@Sender CommandSender sender) {
        if (sender instanceof Player) {
            if (!ManhuntGameManager.isPlayerInGame((Player) sender)) {

            } else {
                sender.sendMessage(ChatColor.RED + "You are already in a another game");
            }
        }
    }
}
