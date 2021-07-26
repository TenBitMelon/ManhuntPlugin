package me.melonboy10.manhuntplugin.commands;

import me.melonboy10.manhuntplugin.ManhuntGame;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player || sender instanceof BlockCommandSender) {
            ManhuntGame.createGame(sender);
            return true;
        }
        return true;
    }

}
