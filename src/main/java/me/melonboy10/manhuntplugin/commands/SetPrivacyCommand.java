package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.game.ManhuntGameSettings;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import org.bukkit.entity.Player;

public class SetPrivacyCommand {

    @Command(name = "", usage = "/privacy", desc = "Sets the privacy of a game")
    public void root(@Sender Player player, ManhuntGameSettings.Privacy privacy) {
        if (ManhuntGameManager.isPlayerInGame(player)) {
            ManhuntGame game = ManhuntGameManager.getGame(player);
            if (game.getCreator().equals(player)) {
                game.getSettings().setPrivacy(privacy);
            } else {
                MessageUtils.sendError(player, "You are not the creator!");
            }
        } else {
            MessageUtils.sendError(player, "You are not in a game!");
        }
    }
}
