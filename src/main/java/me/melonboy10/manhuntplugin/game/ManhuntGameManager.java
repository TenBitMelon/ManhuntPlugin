package me.melonboy10.manhuntplugin.game;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManhuntGameManager {

    private static final List<ManhuntGame> games = new ArrayList<>();
    private static final HashMap<Player, ManhuntGame> playerToGame = new HashMap<>();

    public static void add(ManhuntGame game) {
        games.add(game);
        for (Player player : game.getPlayers().keySet()) {
            playerJoinGame(player, game);
        }
    }

    public static void playerJoinGame(Player player, ManhuntGame game) {
        playerToGame.put(player, game);
    }

    public static List<ManhuntGame> getGames() {
        return games;
    }

    public static ManhuntGame getGame(Player player) {
        return playerToGame.get(player);
    }

    public static ManhuntGame getGame(int hashCode) {
        return games.stream().filter(manhuntGame -> manhuntGame.hashCode() == hashCode).findFirst().orElse(null);
    }

    public static boolean isPlayerInGame(Player player) {
        return playerToGame.containsKey(player);
    }

    public static HashMap<Player, ManhuntGame> getPlayerToGame() {
        return playerToGame;
    }

    public static boolean isPlayerInGame(Player player, ManhuntGame manhuntGame) {
        return !playerToGame.get(player).equals(manhuntGame);
    }

    public static void playerLeaveGame(Player player) {
        playerToGame.remove(player);
    }

    public static void clearGames() {
        playerToGame.forEach((player, manhuntGame) -> {
            playerLeaveGame(player);
            ManhuntPlugin.sendPlayertoHub(player);
        });
        for (ManhuntGame game : games) {
            game.shutDown();
        }
    }
}
