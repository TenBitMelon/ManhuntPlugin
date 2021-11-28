package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import org.bukkit.entity.Player;

public class InviteCommand {

    @Command(name = "", desc = "Used to invite a player to your game!", usage = "<player>")
    public void root(@Sender Player sender, Player player) {
        if (!ManhuntGameManager.isPlayerInGame(sender)) {
            MessageUtils.sendError(sender, "You are not in a game!");
            return;
        }
        ManhuntGame game = ManhuntGameManager.getGame(sender);
        if (game.getCreator().equals(sender)) {
            if (!player.isOnline()) {
                MessageUtils.sendError(sender, "That player doesn't exist or is offline!");
                return;
            }
            if (player == sender) {
                MessageUtils.sendError(sender, "You cannot invite yourself!");
                return;
            }
            game.invitePlayer(player);
            if (ManhuntGameManager.isPlayerInGame(player)) {
                MessageUtils.sendWarning(sender, "That player is already in a game. The invite was still sent!");
            } else {
                MessageUtils.sendSuccess(sender, "The invite was sent!");
            }
        } else {
            MessageUtils.sendError(sender, "You are not the creator of the game!");
        }
    }
}
