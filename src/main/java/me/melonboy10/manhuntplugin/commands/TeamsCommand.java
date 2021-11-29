package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import org.bukkit.entity.Player;

public class TeamsCommand{

     /*
     * This is for the clicking in the text menu
     * This is also for displaying the teams menu while in a game
     */

    @Command(name = "", desc = "Open the teams menu")
    public void root(@Sender Player player) {
        player.sendMessage("TODO: Make menu!");
    }

    @Command(name = "runner", desc = "Join the Runners")
    public void runner(@Sender Player player) {
        if (ManhuntGameManager.isPlayerInGame(player)) {
            ManhuntGame game = ManhuntGameManager.getGame(player);
            if (game.getState().equals(ManhuntGame.GameState.GENERATING)) {
                if (game.getInvitedPlayers().contains(player)) {
                    game.getTeamTextMenu().playerJoinTeam(player, ManhuntGame.Team.RUNNER);
                } else {
                    MessageUtils.sendError(player, "You are not invited!");
                }
            }
        } else {
            MessageUtils.sendError(player, "You are not in a game!");
        }
    }

    @Command(name = "hunter", desc = "Join the Hunters")
    public void hunter(@Sender Player player) {
        if (ManhuntGameManager.isPlayerInGame(player)) {
            ManhuntGame game = ManhuntGameManager.getGame(player);
            if (game.getState().equals(ManhuntGame.GameState.GENERATING)) {
                if (game.getInvitedPlayers().contains(player)) {
                    game.getTeamTextMenu().playerJoinTeam(player, ManhuntGame.Team.HUNTER);
                } else {
                    MessageUtils.sendError(player, "You are not invited!");
                }
            }
        } else {
            MessageUtils.sendError(player, "You are not in a game!");
        }
    }

    @Command(name = "spectator", desc = "Watch as a spectator")
    public void spectator(@Sender Player player) {
        if (ManhuntGameManager.isPlayerInGame(player)) {
            ManhuntGame game = ManhuntGameManager.getGame(player);
            if (game.getState().equals(ManhuntGame.GameState.GENERATING)) {
                if (game.getInvitedPlayers().contains(player)) {
                    game.getTeamTextMenu().playerJoinTeam(player, ManhuntGame.Team.SPECTATOR);
                } else {
                    MessageUtils.sendError(player, "You are not invited!");
                }
            }
        } else {
            MessageUtils.sendError(player, "You are not in a game!");
        }
    }

}
