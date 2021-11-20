package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import org.bukkit.entity.Player;

public class LeaveCommand {

    @Command(name = "", aliases = {"l"}, desc = "Leave the manhunt game.")
    public void root(@Sender Player player) {
        if (ManhuntGameManager.isPlayerInGame(player)) {
            ManhuntGameManager.getGame(player).playerLeave(player);
            MessageUtils.sendSuccess(player, "Sending you to the hub!");
        } else if (!player.getWorld().equals(ManhuntPlugin.hubWorld)) {
            MessageUtils.sendWarning(player, "You are not in a game but also not in the hub.");
            MessageUtils.sendSuccess(player, "Sending you to the hub!");
            ManhuntPlugin.sendPlayertoHub(player);
        }
    }

}
