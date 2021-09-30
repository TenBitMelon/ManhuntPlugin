package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.entity.Player;

import static me.melonboy10.manhuntplugin.game.ManhuntGame.GameState.GENERATING;

public class ReadyCommand {

    @Command(name = "", desc = "Used to ready and unready in a game!")
    public void root(@Sender Player player) {
        //TODO: Ready not working when player /joins the game
        if (ManhuntGameManager.isPlayerInGame(player)) {
            ManhuntGame game = ManhuntGameManager.getGame(player);
            if (game.getState().equals(GENERATING)) {
                game.getTeamTextMenu().playerReady(player);
            }
        }
    }
}
