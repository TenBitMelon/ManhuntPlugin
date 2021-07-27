package me.melonboy10.manhuntplugin;

import me.melonboy10.manhuntplugin.menuSystem.Menu;
import me.melonboy10.manhuntplugin.menuSystem.menus.CreateGameMenu;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MinecraftFont;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static void startWorldGen() {

        spectators.addAll(Bukkit.getOnlinePlayers());

//                ManhuntGame.world = new WorldCreator(String.valueOf(new Random().nextInt()))
//                        .type(ManhuntGame.worldType)
//                        .seed(ManhuntGame.seed)
//                        .createWorld();
//                for (HumanEntity viewer : this.getInventory().getViewers()) {
//                    viewer.closeInventory();
//                    viewer.openInventory(ManhuntGame.teamsMenu.getInventory());
//                }

        sendTeamsMessage();
    }

    public static void sendTeamsMessage() {

//                    TextComponent part1 = new TextComponent(ChatColor.GREEN + "A Manhunt world is being created. Join a team by typing");
//                    TextComponent command = new TextComponent(ChatColor.GREEN + " /teams ");
//                    TextComponent part2 = new TextComponent(ChatColor.GREEN + "or by clicking");
//                    TextComponent menuLink = new TextComponent(ChatColor.GREEN + " here");

                /*
                 +-----------------------------------------+
                .|                                                 |
                .|   Online Players:                               .|
                .|   melonboy10, Enderlord0042, Pick3lbo1,        |
                .|   Minecraft_Atom, Derftcahuji                  .|
                .|                                                 |
                .|   A World is Being Generated!                  .|
                .|   Join a team below or by clicking here!       .|
                .|                                                 |
                .|   Runners - 1  Hunters - 1  Spectators - 3  |
                .|                                                 |
                +-----------------------------------------+
                 */

                /*TextComponent part1 = tec(ChatColor.YELLOW + "+-----------------------------------------+");
                TextComponent part2 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|                                                 |");
                TextComponent part3 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "Online Players:" + ChatColor.DARK_GRAY +
                        "                               " + ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|");
                TextComponent part4 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "melonboy10, Enderlord0042, Pick3lbo1,        |");
                TextComponent part5 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "Minecraft_Atom, Derftcahuji                  .|");
                TextComponent part6 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "|");
                TextComponent part7 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "A World is Being Generated!                  .|");
                TextComponent part8 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "Join a team below or by clicking here!       .|");
                TextComponent part9 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "|");
                TextComponent part10 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "Runners - 1  Hunters - 1  Spectators - 3  |");
                TextComponent part11 = tec(ChatColor.DARK_GRAY + "." + ChatColor.YELLOW + "|   " +
                        "|");
                TextComponent part12 = tec("+-----------------------------------------+");*/

        ArrayList<String> players = new ArrayList<>(){{
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (ManhuntGame.runners.contains(player)) {
                    add(ChatColor.GREEN + player.getName());
                } else if (ManhuntGame.hunters.contains(player)) {
                    add(ChatColor.RED + player.getName());
                } else {
                    add(ChatColor.GRAY + player.getName());
                }
            }
        }};


        /* 257 Width for top row 52
         * | - 1    . - 1    ! - 1    : - 1
         * + - 5    - - 5   " "- 3
         */

        StringBuilder previousPlayerStack = new StringBuilder(".|   " + players.get(0));
        players.remove(0);
        for (String player : players) {
            if (MinecraftFont.Font.getWidth(previousPlayerStack + player) < 257) {
                previousPlayerStack.append(player);
                players.remove(player);
            }
        }
        int widthLeft = 257 - MinecraftFont.Font.getWidth(previousPlayerStack.toString());
        previousPlayerStack.append(" ".repeat(widthLeft / 4))
                .append(".".repeat(widthLeft % 4))
                .append("|");

        TextComponent part1  = tec("+-----------------------------------------+");
        TextComponent part2  = tec(".|" + " ".repeat(62) + ".|");
        TextComponent part3  = tec(".|   " + "Online Players" + ":" + " ".repeat(41) + "|");
        TextComponent part4  = tec(previousPlayerStack.toString());
        TextComponent part5  = tec(".|   Minecraft_Atom, Derftcahuji                  .|");
        TextComponent part6  = tec(".|                                                 |");
        TextComponent part7  = tec(".|   A World is Being Generated!                  .|");
        TextComponent part8  = tec(".|   Join a team below or by clicking here!       .|");
        TextComponent part9  = tec(".|                                                 |");
        TextComponent part10 = tec(".|   Runners - 1  Hunters - 1  Spectators - 3  |");
        TextComponent part11 = tec(".|                                                 |");
        TextComponent part12 = tec("+-----------------------------------------+");

        ArrayList<String> output = new ArrayList<>(){{
            add("");
            add("Online Players:");
            add("melonboy10, Enderlord0042, Pick3lbo1, Minecraft_Atom, Derftcahuji");
            add("");
            add("A World is Being Generated!");
            add("Join a team below or by clicking here!");
            add("");
            add("Runners - 1  Hunters - 1  Spectators - 3");
            add("");
        }};



        for (Player player : Bukkit.getOnlinePlayers()) {
//            onlinePlayer.spigot().sendMessage(part1);
//            onlinePlayer.spigot().sendMessage(part2);
//            onlinePlayer.spigot().sendMessage(part3);
//            onlinePlayer.spigot().sendMessage(part4);
//            onlinePlayer.spigot().sendMessage(part5);
//            onlinePlayer.spigot().sendMessage(part6);
//            onlinePlayer.spigot().sendMessage(part7);
//            onlinePlayer.spigot().sendMessage(part8);
//            onlinePlayer.spigot().sendMessage(part9);
//            onlinePlayer.spigot().sendMessage(part10);
//            onlinePlayer.spigot().sendMessage(part11);
//            onlinePlayer.spigot().sendMessage(part12);
            player.sendMessage("+-----------------------------------------+");
            for (String line : output) {
                formattedMessage(line, player);
            }
            player.sendMessage("+-----------------------------------------+");
        }
    }

    private static void formattedMessage(String line, Player player) {
        int width = MinecraftFont.Font.getWidth(line);
        int lineLength = 235;
        if (width > lineLength) {
            String[] split = line.split(", ");
            String[] segments = split;
            while (width > lineLength) {
                segments = Arrays.copyOf(segments, segments.length - 1);
                width = MinecraftFont.Font.getWidth(Arrays.toString(segments).replaceAll("[\\[\\]]", ""));
            }
            player.sendMessage(".|   " + Arrays.toString(segments).replaceAll("[\\[\\]]", "") + " ".repeat((lineLength - width) / 4) + ".".repeat((lineLength - width) % 4) + "|");
            formattedMessage(Arrays.toString(Arrays.copyOfRange(split, split.length - segments.length + 1, split.length)).replaceAll("[\\[\\],]", ""), player);
        } else {
            player.sendMessage(".|   " + line + " ".repeat((lineLength - width) / 4) + ".".repeat((lineLength - width) % 4) + "|");
        }
    }

    private static TextComponent tec(String string) {
        return new TextComponent(string);
    }
}
