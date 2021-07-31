package me.melonboy10.manhuntplugin;

import me.melonboy10.manhuntplugin.menuSystem.Menu;
import me.melonboy10.manhuntplugin.menuSystem.menus.CreateGameMenu;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MinecraftFont;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class ManhuntGameOld {

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

    public ManhuntGameOld(ManhuntPlugin plugin) {
        ManhuntGameOld.plugin = plugin;
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
        if (worldGenerating) {
            if (sender instanceof Player) {
                teamsMenu.open(((Player) sender));
            }
        }

        worldType = WorldType.NORMAL;
        difficulty = Difficulty.NORMAL;
        seed = new Random().nextLong();

        inCreation = true;
        creationMenu = new CreateGameMenu(plugin, "");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            creationMenu.open(onlinePlayer);
        }
    }

    public static void startWorldGen() {
        spectators.clear();
        spectators.addAll(Bukkit.getOnlinePlayers());

//                ManhuntGame.world = new WorldCreator(String.valueOf(new Random().nextInt()))
//                        .type(ManhuntGame.worldType)
//                        .seed(ManhuntGame.seed)
//                        .createWorld();
//                for (HumanEntity viewer : this.getInventory().getViewers()) {
//                    viewer.closeInventory();
//                    viewer.openInventory(ManhuntGame.teamsMenu.getInventory());
//                }

        inCreation = false;
        worldGenerating = true;
        sendTeamsMessage();
    }

    public static void sendTeamsMessage() {
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

        ArrayList<String> players = new ArrayList<>(){{
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (runners.contains(player)) {
                    add(ChatColor.GREEN + player.getName());
                } else if (hunters.contains(player)) {
                    add(ChatColor.RED + player.getName());
                } else {
                    add(ChatColor.GRAY + player.getName());
                }
            }
        }};

        ArrayList<TextComponent> preset = new ArrayList<>(){{
            add(componentToText(new ComponentBuilder("").create()));
            add(componentToText(new ComponentBuilder(ChatColor.AQUA + "Online Players" + ChatColor.DARK_AQUA + ":").create()));
            add(componentToText(new ComponentBuilder(players.stream().reduce((a, b) -> a + b).get()).create()));
            add(componentToText(new ComponentBuilder("").create()));
            add(componentToText(new ComponentBuilder(ChatColor.AQUA + "A World is Being Generated!").create()));
            add(componentToText(new ComponentBuilder(ChatColor.AQUA + "Join a team below or by ")
                .append("clicking here")
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "/teams")
                    ))
                    .event(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND, "/teams"
                    ))
                    .color(ChatColor.GOLD.asBungee())
                .append(ChatColor.DARK_AQUA + "!")
                .create()));
            add(componentToText(new ComponentBuilder("").create()));
            add(componentToText(new ComponentBuilder(ChatColor.GREEN + "Runners")
                    .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Join runners")
                    ))
                    .event(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND, "/teams runner"
                    ))
                    .append(ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + runners.size())
                    .append(ChatColor.RED +  "  Hunters")
                    .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED +  "Hunters")
                    ))
                    .event(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND, "/teams hunter"
                    ))
                    .append(ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + hunters.size())
                    .append(ChatColor.GRAY + "  Spectators")
                    .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Spectators")
                    ))
                    .event(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND, "/teams spectator"
                    ))
                    .append(ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + spectators.size())
                    .create()
            ));
            add(componentToText(new ComponentBuilder("").create()));
        }};

        ArrayList<TextComponent> output = new ArrayList<>();

        for (TextComponent line : preset) {
            formattedMessage(line, output);
        }

        for (TextComponent line : output) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.YELLOW + "+-----------------------------------------+");
                player.spigot().sendMessage(line);
                player.sendMessage(ChatColor.YELLOW + "+-----------------------------------------+");
            }
        }

    }

    private static void formattedMessage(TextComponent line, List<TextComponent> list) {
        int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText()));
        int lineLength = 235;
        if (width > lineLength) {
            String[] split = line.getText().split(", ");
            String[] segments = split;
            while (width > lineLength) {
                segments = Arrays.copyOf(segments, segments.length - 1);
                width = MinecraftFont.Font.getWidth(ChatColor.stripColor(
                    Arrays.toString(segments)
                        .replaceAll("[\\[\\]]", "")
                ));
            }
            list.add(componentToText(
                new ComponentBuilder(".").color(ChatColor.DARK_GRAY.asBungee())
                    .append("|   ").color(ChatColor.YELLOW.asBungee())
                    .append(Arrays.toString(segments).replaceAll("[\\[\\]]", ""))
                    .append(" ".repeat((lineLength - width) / 4))
                    .append(".".repeat((lineLength - width) % 4)).color(ChatColor.DARK_GRAY.asBungee())
                    .append("|").color(ChatColor.YELLOW.asBungee())
                    .create()));
            formattedMessage(new TextComponent(Arrays.toString(
                Arrays.copyOfRange(
                    split, split.length - segments.length + 1, split.length))
                .replaceAll("[\\[\\],]", "")), list);
        } else {
            list.add(componentToText(
                new ComponentBuilder(".").color(ChatColor.DARK_GRAY.asBungee())
                    .append("|   ").color(ChatColor.YELLOW.asBungee())
                    .append(line)
                    .append(" ".repeat((lineLength - width) / 4))
                    .append(".".repeat((lineLength - width) % 4)).color(ChatColor.DARK_GRAY.asBungee())
                    .append("|").color(ChatColor.YELLOW.asBungee())
                    .create()));
        }
    }

    private static TextComponent componentToText(BaseComponent[] components) {
        TextComponent textComponent = new TextComponent();
        for (BaseComponent component : components) {
            textComponent.addExtra(component);
        }
        return textComponent;
    }
}
