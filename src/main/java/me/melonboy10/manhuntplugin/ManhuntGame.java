package me.melonboy10.manhuntplugin;

import me.melonboy10.manhuntplugin.menuSystem.Menu;
import me.melonboy10.manhuntplugin.menuSystem.menus.CreateGameMenu;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public final class ManhuntGame {

    private static ManhuntPlugin plugin = ManhuntPlugin.plugin;

    public static boolean inGame = false;
    public static boolean inCreation = false;
    public static boolean worldGenerating = false;

    public static Menu creationMenu;
    public static Menu teamsMenu;

    public static World world;
    public static WorldType worldType;
    public static Difficulty difficulty;
    public static long seed;

    public static ArrayList<Player> runners = new ArrayList<>();
    public static ArrayList<Player> hunters = new ArrayList<>();
    public static ArrayList<Player> spectators = new ArrayList<>();

    public ManhuntGame(ManhuntPlugin plugin) {
        ManhuntGame.plugin = plugin;
    }

    public static void createGame(CommandSender sender) {
        if (inGame) {
            sender.sendMessage(ChatColor.RED + "A game in currently being played!");
            sender.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "You can join as a spectator by clicking " + ChatColor.GREEN + "here!");
            return;
        }
        if (inCreation) {
            if (sender instanceof Player) {
                creationMenu.open(((Player) sender));
            }
            return;
        }

        worldType = WorldType.NORMAL;
        difficulty = Difficulty.NORMAL;
        seed = new Random().nextLong();

        inCreation = true;
        creationMenu = new CreateGameMenu(plugin);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            creationMenu.open(onlinePlayer);
        }
    }

}
