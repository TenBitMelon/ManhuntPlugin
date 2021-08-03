package me.melonboy10.manhuntplugin.game;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.melonboy10.manhuntplugin.game.ManhuntGame.Team.*;

public class ManhuntGameManager {

    private static final List<ManhuntGame> games = new ArrayList<>();
    private static final HashMap<Player, ManhuntGame> playerToGame = new HashMap<>();

    public static ManhuntGame create(ManhuntGameSettings settings, ArrayList<Player> invitedPlayers, Player creator) {
        ManhuntGame game = new ManhuntGame(settings);
        invitedPlayers.forEach(game::invitePlayer);
        game.playerJoin(creator, SPECTATOR);

        games.add(game);
        return game;
    }

    public static List<ManhuntGame> getGames() {
        return games;
    }

    public static ManhuntGame getGame(Player player) {
        return playerToGame.get(player);
    }

    public static boolean isPlayerInGame(Player player) {
        return playerToGame.containsKey(player);
    }

    public static HashMap<Player, ManhuntGame> getPlayerToGame() {
        return playerToGame;
    }
}
