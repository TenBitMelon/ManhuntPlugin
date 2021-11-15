package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import org.bukkit.entity.Player;

public class JoinCommand {

    @Command(name = "", desc = "Used to join a game.")
    public void root(@Sender Player sender, int gameID) {
        if (!ManhuntGameManager.isPlayerInGame(sender)) {
            ManhuntGame game = ManhuntGameManager.getGame(gameID);
            if (game != null) {
                if (game.getInvitedPlayers().contains(sender)) {
                    game.playerAcceptInvite(sender.getPlayer());
                } else {
                    MessageUtils.sendError(sender, "You are not invited!");
                }
            } else {
                MessageUtils.sendError(sender, "That game doesn't exist!");
            }
        } else {
            MessageUtils.sendError(sender, "You are already in another game!");
        }
    }
}
