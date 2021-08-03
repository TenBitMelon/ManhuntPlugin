package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.ManhuntGameOld;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamsCommand{

     /*
     * This is for the clicking in the text menu
     * This is also for displaying the teams menu why in a game
     */

    @Command(name = "", aliases = {"join"}, desc = "Open the teams menu")
    public void root(@Sender CommandSender sender) {
        if (sender instanceof Player) {
            ManhuntGameOld.teamsMenu.open(((Player) sender));
        }
    }

    @Command(name = "runner", desc = "Join the Runners")
    public void runner(@Sender CommandSender sender) {
        if (sender instanceof Player) {

        }
    }

    @Command(name = "hunter", desc = "Join the Hunters")
    public void hunter(@Sender CommandSender sender) {
        if (sender instanceof Player) {

        }
    }

    @Command(name = "spectator", desc = "Watch as a spectator")
    public void spectator(@Sender CommandSender sender) {
        if (sender instanceof Player) {

        }
    }

}
