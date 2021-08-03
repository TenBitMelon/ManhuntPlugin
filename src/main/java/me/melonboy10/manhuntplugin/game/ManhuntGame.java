package me.melonboy10.manhuntplugin.game;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.menuSystem.menus.TeamSelectMenu;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MinecraftFont;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ManhuntGame {

    private static final ManhuntPlugin plugin = ManhuntPlugin.plugin;

    public enum Team {RUNNER, HUNTER, SPECTATOR;}
    public enum GameState {GENERATING, HUNTER_COOLDOWN, PLAYING, GAME_OVER;}

    private GameState gameState = GameState.GENERATING;
    private ManhuntGameSettings settings;
    private World world;

    private int hunterCooldown;

    private TeamSelectTextMenu teamTextMenu;
    private TeamSelectMenu teamMenu;
    private final ArrayList<Player> invitedPlayers = new ArrayList<>();
    private final HashBiMap<Player, Team> players = HashBiMap.create();

    private final HashMap<Player, PlayerInventory> quitPlayerInventory = new HashMap<>();
    private final HashMap<Player, Location> quitPlayerLocation = new HashMap<>();

    public ManhuntGame(ManhuntGameSettings settings) {
        this.settings = settings;
        hunterCooldown = settings.getHunterCooldown();

        //generate world
    }

    public Team getTeam(Player player) {
        return players.getOrDefault(player, null);
    }

    public void invitePlayer(Player player) {
        invitedPlayers.add(player);
        player.sendMessage(ChatColor.YELLOW + "+-----------------------------------------+");
        player.spigot().sendMessage(formatMessage(new TextComponent(ChatColor.GREEN + "You have been invited to a new Manhunt"), 235));
        if (ManhuntGameManager.isPlayerInGame(player)) {
            player.spigot().sendMessage(formatMessage(new TextComponent(ChatColor.RED + "You are already in a game so to join you"), 235));
            player.spigot().sendMessage(formatMessage(componentToText(
                new ComponentBuilder(ChatColor.RED + "need to leave your game by typing ")
                    .append("/leave")
                    .color(ChatColor.GOLD.asBungee())
                    .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "/leave")
                    ))
                    .event(new ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND, "/leave"
                    ))
                    .append(".")
                    .color(ChatColor.RED.asBungee())
                    .create()), 235)
            );
        }
        player.spigot().sendMessage(formatMessage(new TextComponent(ChatColor.YELLOW + "You can join by typing or clicking /join " + this.hashCode()), 235));
        player.sendMessage(ChatColor.YELLOW + "+-----------------------------------------+");
    }

    public void playerJoin(Player player, Team team) {
        if (!ManhuntGameManager.isPlayerInGame(player)) {
            if (quitPlayerInventory.containsKey(player) && quitPlayerLocation.containsKey(player)) {
                player.teleport(quitPlayerLocation.get(player));
                player.getInventory().setContents(quitPlayerInventory.get(player).getContents());
            } else {
                switch (settings.getPrivacy()) {
                    case PUBLIC -> {
                        switch (gameState) {
                            case GENERATING: {
                                if (invitedPlayers.contains(player)) {
                                    teamTextMenu.addPlayer(player);
                                } else {
                                    invitePlayer(player);
                                }
                            }
                            case HUNTER_COOLDOWN: {
                                switch (team) {
                                    case RUNNER -> {
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                    case HUNTER -> {
                                        player.setGameMode(GameMode.SURVIVAL);
                                        player.addPotionEffects(new ArrayList<>() {{
                                            add(new PotionEffect(PotionEffectType.SLOW_DIGGING, hunterCooldown, 255, false, false, false));
                                            add(new PotionEffect(PotionEffectType.BLINDNESS, hunterCooldown, 255, false, false, false));
                                        }});
                                    }
                                    case SPECTATOR -> {
                                        player.setGameMode(GameMode.SPECTATOR);
                                    }
                                }
                                players.put(player, team);
                                player.teleport(world.getSpawnLocation());
                                player.getInventory().clear();
                            }
                            case PLAYING: {
                                if (team == Team.SPECTATOR) {
                                    player.setGameMode(GameMode.SPECTATOR);
                                }
                                players.put(player, team);
                                player.teleport(world.getSpawnLocation());
                                player.getInventory().clear();
                            }
                            case GAME_OVER: {
                                MessageUtils.sendError(player, "This game is over!");
                            }
                        }
                    }
                    case SPECTATOR_ONLY -> {
                        if (!gameState.equals(GameState.GAME_OVER)) {
                            player.teleport(world.getSpawnLocation());
                            players.put(player, Team.SPECTATOR);
                            player.getInventory().clear();
                            player.setGameMode(GameMode.SPECTATOR);
                        } else {
                            MessageUtils.sendError(player, "This game is over!");
                        }
                    }
                    case PRIVATE -> {
                        MessageUtils.sendError(player, "This game is private!");
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are currently in another game!");
        }
    }

    public void playerLeave(Player player) {
        Team team = getTeam(player);
        if (team.equals(Team.RUNNER) || team.equals(Team.HUNTER)) {
            quitPlayerInventory.put(player, player.getInventory());
            quitPlayerLocation.put(player, player.getLocation());
        }
        player.getInventory().clear();
        player.teleport(ManhuntPlugin.hubWorld.getSpawnLocation());
    }

    private TextComponent formatMessage(TextComponent line, int length) {
        int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText()));
        if (width > length) {
            return componentToText(
                    new ComponentBuilder(".").color(ChatColor.DARK_GRAY.asBungee())
                            .append("|   ").color(ChatColor.YELLOW.asBungee())
                            .append(line)
                            .append("|").color(ChatColor.YELLOW.asBungee())
                            .create());
        } else {
            return componentToText(
                    new ComponentBuilder(".").color(ChatColor.DARK_GRAY.asBungee())
                            .append("|   ").color(ChatColor.YELLOW.asBungee())
                            .append(line)
                            .append(" ".repeat((length - width) / 4))
                            .append(".".repeat((length - width) % 4)).color(ChatColor.DARK_GRAY.asBungee())
                            .append("|").color(ChatColor.YELLOW.asBungee())
                            .create());
        }
    }

    private void formatMessage(TextComponent line, List<TextComponent> list) {
        int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText()));
        int length = 235;
        if (width > length) {
            String[] split = line.getText().split(", ");
            String[] segments = split;
            while (width > length) {
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
                            .append(" ".repeat((length - width) / 4))
                            .append(".".repeat((length - width) % 4)).color(ChatColor.DARK_GRAY.asBungee())
                            .append("|").color(ChatColor.YELLOW.asBungee())
                            .create()));
            formatMessage(new TextComponent(Arrays.toString(
                    Arrays.copyOfRange(
                            split, split.length - segments.length + 1, split.length))
                    .replaceAll("[\\[\\],]", "")), list);
        } else {
            list.add(componentToText(
                    new ComponentBuilder(".").color(ChatColor.DARK_GRAY.asBungee())
                            .append("|   ").color(ChatColor.YELLOW.asBungee())
                            .append(line)
                            .append(" ".repeat((length - width) / 4))
                            .append(".".repeat((length - width) % 4)).color(ChatColor.DARK_GRAY.asBungee())
                            .append("|").color(ChatColor.YELLOW.asBungee())
                            .create()));
        }
    }

    private TextComponent componentToText(BaseComponent[] components) {
        TextComponent textComponent = new TextComponent();
        for (BaseComponent component : components) {
            textComponent.addExtra(component);
        }
        return textComponent;
    }

    /*
     * Player Road Map - the player experience
     *
     * Player Joining to server
     * creates new game
     * options menu - can invite players
     * creates world
     * - new instance of game with settings (menu holds settings)
     * - Message sent to all members of party list - contains item preview of map world
     * - players can join teams
     * World gets generated an players get sent to world - blindness for all
     * grace period counts down - remove blindness from just runners
     * game runs normally
     * - hunter die and respawn
     * - all runners dies / leave and game over2
     * - enderdragon killed (detect advancement or mob kill) game over
     * - players leaves (freeze the game if possible) 5 mins to come back
     * - Game Over: teleport all players back after 15 seconds (players can leave during this stage)
     * - Save game to players history
     * - remove from current games and delete world
     *
     * Players joins server
     * opens current game menu
     * shows game all games in progress
     *
     */
    /*
     * invite command / button in menu
     *
     * Text team joining menu for players in joiningPlayers before the world is generated
     * Team menu GUI for after clicking in the all games menu
     * (runners, hunters, spectators, with options to join all)
     * invited but not joined players become dark gray
     * have a ready button for when players are ready (put a check or an x next to name)
     *
     * All games menu - for all currently playing games
     * Shows: Generating, Public, Private, Allowing spectators
     *
     * joiningPlayers - list of players to show the joining text
     * show message to all players saying (a game has been created click to join)
     *
     * Spectators - only for people to teleport in and currently spectating people
     * (added during team joining & people joining to spectate)
     *
     * Runners - a
     */
}
