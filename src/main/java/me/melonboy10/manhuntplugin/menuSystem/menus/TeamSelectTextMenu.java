package me.melonboy10.manhuntplugin.menuSystem.menus;

import com.google.common.collect.HashBiMap;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static org.bukkit.ChatColor.*;

public class TeamSelectTextMenu {

    private final ManhuntGame game;
    // Players who have joined the game
    private final HashBiMap<Player, ManhuntGame.Team> players = HashBiMap.create();

    public TeamSelectTextMenu(ManhuntGame game, ItemStack item) {
        this.game = game;
    }

    // Invited players
    private ArrayList<Player> invitedPlayers() {
        return game.getInvitedPlayers();
    }

    public void update() {
        for (Player player : players.keySet()) {
            MessageUtils.sendLineBreak(player);

            MessageUtils.sendFormattedMessage(player,
                new ComponentBuilder("Status").color(ChatColor.AQUA)
                    .append(":").color(ChatColor.DARK_GRAY)
                    .append("     🌎").color(game.isWorldReady() ? ChatColor.GREEN : ChatColor.GRAY)
                    .append("  📄").color(players.size() == invitedPlayers().size() ? ChatColor.GREEN : ChatColor.GRAY)
                    .create()
            );
            MessageUtils.sendBlankLine(player);
            MessageUtils.sendWrappedMessage(player,
                new TextComponent()
            );



            MessageUtils.sendLineBreak(player);
        }
    }

    private String getPlayerList() {
        LinkedHashSet<Player> players = new LinkedHashSet<>();
        players.addAll(invitedPlayers());
        players.addAll(players);

        return "NOT DONE! TODO: THIS";
    }

    public void playerAcceptInvite(Player player) {
        players.put(player, ManhuntGame.Team.SPECTATOR);
    }
}
