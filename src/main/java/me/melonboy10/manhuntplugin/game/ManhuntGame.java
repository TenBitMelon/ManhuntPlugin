package me.melonboy10.manhuntplugin.game;

import com.google.common.collect.HashBiMap;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.menuSystem.menus.TeamSelectMenu;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ManhuntGame {

    private static final ManhuntPlugin plugin = null;

    public enum Team {RUNNER, HUNTER, SPECTATOR}
    public enum GameState {GENERATING, HUNTER_COOLDOWN, PLAYING, GAME_OVER}

    private GameState gameState = GameState.GENERATING;

    // Players to show joining text and teleport; This is not players who were invited;
    private ArrayList<Player> joiningPlayers = new ArrayList<Player>();
    private TeamSelectTextMenu teamTextMenu;
    private TeamSelectMenu teamMenu;

    private HashBiMap<Player, Team> players = HashBiMap.create();
    //Players spectating (during game);
    private ArrayList<Player> spectators = new ArrayList<Player>();

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
     * - all runners dies / leave and game over
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
