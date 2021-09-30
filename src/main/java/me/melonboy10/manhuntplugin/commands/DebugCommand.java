package me.melonboy10.manhuntplugin.commands;

    import com.jonahseguin.drink.annotation.Command;
    import com.jonahseguin.drink.annotation.Sender;
    import me.melonboy10.manhuntplugin.game.ManhuntGame;
    import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
    import me.melonboy10.manhuntplugin.menuSystem.menus.TeamSelectTextMenu;
    import me.melonboy10.manhuntplugin.utils.MessageUtils;
    import net.md_5.bungee.api.chat.TextComponent;
    import org.bukkit.ChatColor;
    import org.bukkit.command.CommandSender;
    import org.bukkit.entity.Player;

public class DebugCommand {

    @Command(name = "", desc = "Used to debug the game", usage = "")
    public void root(@Sender Player sender, Player player) {

       MessageUtils.Builder builder = new MessageUtils.Builder(sender)
           .lineBreak()
           .blank()
           .keyVal("Player", player.getName())
           .keyVal("Is player in game", ManhuntGameManager.isPlayerInGame(player));
       if (ManhuntGameManager.isPlayerInGame(player)) {
           ManhuntGame game = ManhuntGameManager.getGame(player);
           builder
               .blank()
               .keyVal("Game id", game.hashCode())
               .keyVal("Game state", game.getState())
               .keyVal("Is world ready", game.isWorldReady())
               .keyVal("Players", game.getPlayers())
               .keyVal("Invited players", game.getInvitedPlayers());
           if (game.getTeamTextMenu() != null) {
               TeamSelectTextMenu textMenu = game.getTeamTextMenu();
               builder
                   .blank()
                   .keyVal("Team Text Menu", textMenu)
                   .keyVal("Players", textMenu.getPlayers());
           }
       }


    }
}
