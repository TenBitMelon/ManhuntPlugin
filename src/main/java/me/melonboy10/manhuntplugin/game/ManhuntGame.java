package me.melonboy10.manhuntplugin.game;

import com.google.common.collect.HashBiMap;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.menuSystem.menus.TeamSelectMenu;
import me.melonboy10.manhuntplugin.menuSystem.menus.TeamSelectTextMenu;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ManhuntGame {

    private static final ManhuntPlugin plugin = ManhuntPlugin.plugin;

    public enum Team {RUNNER, HUNTER, SPECTATOR;}
    public enum GameState {GENERATING, HUNTER_COOLDOWN, PLAYING, GAME_OVER;}

    private GameState gameState = GameState.GENERATING;
    private final ManhuntGameSettings settings;
    private World world;
    private int hunterCooldown;

    private TeamSelectTextMenu teamTextMenu;
    private TeamSelectMenu teamMenu;
    private ItemStack mapItem;
    private final ArrayList<Player> invitedPlayers = new ArrayList<>();
    private final HashBiMap<Player, Team> players = HashBiMap.create();

    private final HashMap<Player, PlayerInventory> quitPlayerInventory = new HashMap<>();
    private final HashMap<Player, Location> quitPlayerLocation = new HashMap<>();
    private final HashMap<Player, Team> quitPlayerTeam = new HashMap<>();
    private final HashMap<Player, BukkitTask> quitPlayerTimer = new HashMap<>();

    public ManhuntGame(ManhuntGameSettings settings, Player creator, LinkedList<Player> invitedPlayers, ItemStack item) {
        this.settings = settings;
        hunterCooldown = settings.getHunterCooldown();
        teamTextMenu = new TeamSelectTextMenu(this, item);
        mapItem = item;

        forceInvitePlayer(creator);
        playerAcceptInvite(creator);
        invitedPlayers.forEach(this::invitePlayer);


        //generate world
    }

    public Team getTeam(Player player) {
        return players.getOrDefault(player, null);
    }

    public ArrayList<Player> getInvitedPlayers() {
        return invitedPlayers;
    }

    public void forceInvitePlayer(Player creator) {
        invitedPlayers.add(creator);
    }

    /**
     * Used to invite a player and show invitee text
     * Checks if in a game and gives warning
     *
     * @param player Player being invited
     */
    public void invitePlayer(Player player) {
        invitedPlayers.add(player);
        teamTextMenu.update();
        MessageUtils.sendLineBreak(player);
        MessageUtils.sendBlankLine(player);
        MessageUtils.sendFormattedMessage(player, new TextComponent(ChatColor.GREEN + "You have been invited to a new Manhunt"));
        MessageUtils.sendFormattedMessage(player, new TextComponent(ChatColor.GREEN + "You have been invited to a new Manhunt"));
        MessageUtils.sendBlankLine(player);
        if (ManhuntGameManager.isPlayerInGame(player)) {
            MessageUtils.sendFormattedMessage(player, new TextComponent(ChatColor.RED + "You are already in a game so to join you"));
            MessageUtils.sendFormattedMessage(player,
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
                    .create()
            );
            MessageUtils.sendBlankLine(player);
        }
        MessageUtils.sendFormattedMessage(player, new TextComponent(ChatColor.YELLOW + "You can join by typing or clicking /join " + this.hashCode()));
        player.sendMessage(ChatColor.YELLOW + "+-----------------------------------------+");
    }

    /**
     * Called when a player accepts the invite
     * Player cannot join if not invited
     *
     * @param player Player joining
     */
    public void playerAcceptInvite(Player player) {
        if (!invitedPlayers.contains(player)) {
            MessageUtils.sendError(player, "You have not been invited!");
            return;
        }
        if (ManhuntGameManager.isPlayerInGame(player)) {
            MessageUtils.sendError(player, "You are already in a game!");
            return;
        }
        if (gameState.equals(GameState.GAME_OVER)) {
            MessageUtils.sendError(player, "This game is over!");
            return;
        }
        if (gameState.equals(GameState.GENERATING)) {
            teamTextMenu.playerAcceptInvite(player);
        } else if (settings.getPrivacy().equals(ManhuntGameSettings.Privacy.SPECTATOR_ONLY)) {
            players.put(player, Team.SPECTATOR);
            teleportIntoGame(player);
        } else {
            if (quitPlayerTimer.containsKey(player)) {
                teleportIntoGame(player);
            } else {
                teamMenu.open(player);
            }
        }
    }

    /**
     * Set Team called when /team in called
     * check if in game
     * check if already on that team
     *
     * need to make independant from menu
     * menu can have selector and set when menu is done
     * but this can be run when someone new joins
     */
    public void setTeams(HashBiMap<Player, ManhuntGame.Team> setPlayers) {
        setPlayers.forEach((player, team) -> {
            if (!ManhuntGameManager.isPlayerInGame(player, this) &&  invitedPlayers.contains(player)) {

            }
        });
    }

    /**
     * Called when teleporting a player into a game
     *
     * @param player Player being teleported
     */
    private void teleportIntoGame(Player player) {
        if (!isWorldReady()) {
            return;
        }
        if (players.containsKey(player) || quitPlayerTimer.containsKey(player)) {
            player.teleport(world.getSpawnLocation());
            if (gameState.equals(GameState.HUNTER_COOLDOWN) && getTeam(player).equals(Team.HUNTER)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, hunterCooldown, 255, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, hunterCooldown, 255, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, hunterCooldown, 255, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, hunterCooldown, 255, false, false));
            }
            if (quitPlayerTimer.containsKey(player)) {
                players.put(player, quitPlayerTeam.get(player));
                player.getInventory().setContents(quitPlayerInventory.get(player).getContents());
                player.teleport(quitPlayerLocation.get(player));
                quitPlayerTimer.get(player).cancel();

                quitPlayerTimer.remove(player);
                quitPlayerTeam.remove(player);
                quitPlayerLocation.remove(player);
                quitPlayerInventory.remove(player);
            }
        }
    }

    public boolean isWorldReady() {
        return Bukkit.getWorld(world.getUID()) != null;
    }

    public void playerLeave(Player player) {
        Team team = getTeam(player);
        if (team.equals(Team.RUNNER) || team.equals(Team.HUNTER) && (!gameState.equals(GameState.GENERATING) && !gameState.equals(GameState.GAME_OVER))) {
            quitPlayerInventory.put(player, player.getInventory());
            quitPlayerLocation.put(player, player.getLocation());
            quitPlayerTeam.put(player, getTeam(player));

            quitPlayerTimer.put(player, new BukkitRunnable() {
                @Override
                public void run() {
                    quitPlayerTimer.remove(player);
                    quitPlayerInventory.remove(player);
                    quitPlayerLocation.remove(player);
                    quitPlayerTeam.remove(player);
                }
            }.runTaskLater(plugin, 5 * 60 * 20));

            players.remove(player);
            players.forEach((player1, team1) -> player1.sendMessage(ChatColor.RED + player1.getName() + " has left the game! They have 5 minutes to rejoin!"));
        }
        player.getInventory().clear();
        player.teleport(ManhuntPlugin.hubWorld.getSpawnLocation());
    }

    public void checkWinConditions() {

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
