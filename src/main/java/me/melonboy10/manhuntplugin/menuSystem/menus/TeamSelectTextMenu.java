package me.melonboy10.manhuntplugin.menuSystem.menus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.utils.ConfigurationSerializableAdapter;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

import static org.bukkit.ChatColor.*;

public class TeamSelectTextMenu {

    private final ManhuntGame game;
    // Players who have joined the game
    private final HashMap<Player, ManhuntGame.Team> players = new HashMap<>();
    private final ArrayList<Player> readyPlayers = new ArrayList<>();
    private final String serializedMap;


    public TeamSelectTextMenu(ManhuntGame game) {
        this.game = game;
        this.serializedMap = "";
    }

    // Invited players
    private ArrayList<Player> invitedPlayers() {
        return game.getInvitedPlayers();
    }

    public void update() { // ☑☒⎆⎘♽
        System.out.println("----------------------------------------------------");
        System.out.println(serializedMap);
        System.out.println("----------------------------------------------------");
        for (Player player : players.keySet()) {
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendEmptyLine(player);
            MessageUtils.sendLineBreak(player);
            MessageUtils.sendFormattedMessage(player,
                new ComponentBuilder("Status").color(ChatColor.AQUA)
                    .append(":").color(ChatColor.DARK_GRAY)
                    .append("    ♻").color(game.isWorldReady() ? ChatColor.GREEN : ChatColor.GRAY)
                    .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_GRAY + "World Generation Done: " + BooleanUtils.toStringYesNo(game.isWorldReady()))
                    ))
                    .append("    ⚔").color(readyPlayers.size() == players.size() ? ChatColor.GREEN : ChatColor.GRAY)
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_GRAY + "Everyone Ready: " + BooleanUtils.toStringYesNo(readyPlayers.size() == players.size()))
                    ))
                    .append("    ⛏").color(ChatColor.DARK_AQUA)
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_ITEM, new Item(Material.MELON_SLICE.toString().toLowerCase(), 1, ItemTag.ofNbt(
                            "{" +
                                "display:{" +
                                    "Name:'{\"text\":\"World Settings\",\"color\":\"yellow\",\"italic\":false}'," +
                                    "Lore:[" +
                                        "'{\"text\":\"Configuration\",\"color\":\"dark_gray\",\"italic\":false}'," +
                                        "'{\"text\":\"\"}'," +
                                        "'[" +
                                            "{\"text\":\"Difficulty\",\"color\":\"aqua\",\"italic\":false}," +
                                            "{\"text\":\":\",\"color\":\"dark_gray\",\"italic\":false}," +
                                            "{\"text\":\" " + game.getSettings().getDifficulty() + "\",\"color\":\"gray\",\"italic\":false}" +
                                        "]'," +
                                        "'[" +
                                            "{\"text\":\"World Type\",\"color\":\"aqua\",\"italic\":false}," +
                                            "{\"text\":\":\",\"color\":\"dark_gray\",\"italic\":false}," +
                                            "{\"text\":\" " + game.getSettings().getWorldType() + "\",\"color\":\"gray\",\"italic\":false}" +
                                        "]'," +
                                        "'[" +
                                            "{\"text\":\"Seed\",\"color\":\"aqua\",\"italic\":false}," +
                                            "{\"text\":\":\",\"color\":\"dark_gray\",\"italic\":false}," +
                                            "{\"text\":\" " + game.getSettings().getSeed() + "\",\"color\":\"gray\",\"italic\":false}" +
                                        "]'," +
                                        "'[" +
                                            "{\"text\":\"Hunter Cooldown\",\"color\":\"aqua\",\"italic\":false}," +
                                            "{\"text\":\":\",\"color\":\"dark_gray\",\"italic\":false}," +
                                            "{\"text\":\" " + game.getSettings().getHunterCooldown() + "\",\"color\":\"gray\",\"italic\":false}" +
                                        "]'," +
                                        "'[" +
                                            "{\"text\":\"Privacy\",\"color\":\"aqua\",\"italic\":false}," +
                                            "{\"text\":\":\",\"color\":\"dark_gray\",\"italic\":false}," +
                                            "{\"text\":\" " + game.getSettings().getPrivacy() + "\",\"color\":\"gray\",\"italic\":false}" +
                                        "]'" +
                                    "]" +
                                "}," +
                                "HideFlags:32" +
                            "}"))
                    )).append("    🛡").color(ChatColor.DARK_AQUA)
//                    .event(new HoverEvent(
//                        HoverEvent.Action.SHOW_ITEM, new Item(Material.MELON_SLICE.toString().toLowerCase(), 1, ItemTag.ofNbt(
//                            gson.toJson(game.getMapItem().getItemMeta())
//                        ))
//                    ))
                    .append("   |   ").color(ChatColor.DARK_GRAY)
                    .append(readyPlayers.contains(player) ? ChatColor.GREEN + "☑": ChatColor.RED + "☒")
                    .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(readyPlayers.contains(player) ? ChatColor.GREEN + "Ready" : ChatColor.RED + "Unready")
                    ))
                    .event(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND, "/ready"
                    ))
                    .create()
            );
            MessageUtils.sendBlankLine(player);
            MessageUtils.sendWrappedMessage(player,
                new TextComponent(getPlayerList())
            );
            MessageUtils.sendBlankLine(player);
            MessageUtils.sendFormattedMessage(player, new ComponentBuilder()
                .append("Runner")
                    .color(GREEN.asBungee())
                    .event(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND, "/teams runner"
                    ))
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Join Runners")
                    ))
                .append(" : ")
                    .color(DARK_GRAY.asBungee())
                .append(players.values().stream().filter(team -> team.equals(ManhuntGame.Team.RUNNER)).count() + "   ")
                    .color(GRAY.asBungee())
                .append("Hunter")
                    .color(RED.asBungee())
                    .event(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND, "/teams hunter"
                    ))
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Join Hunters")
                    ))
                .append(" : ")
                    .color(DARK_GRAY.asBungee())
                .append(players.values().stream().filter(team -> team.equals(ManhuntGame.Team.HUNTER)).count() + "   ")
                    .color(GRAY.asBungee())
                .append("Spectator")
                    .color(GRAY.asBungee())
                    .event(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND, "/teams spectator"
                    ))
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Join Spectators")
                    ))
                .append(" : ")
                    .color(DARK_GRAY.asBungee())
                .append(players.values().stream().filter(team -> team.equals(ManhuntGame.Team.SPECTATOR)).count() + "   ")
                    .color(GRAY.asBungee())
                .create()
            );
            MessageUtils.sendBlankLine(player);
            MessageUtils.sendLineBreak(player);
        }
    }

    private String getPlayerList() {
        Set<Player> tempPlayers = new LinkedHashSet<>();
        tempPlayers.addAll(players.keySet());
        tempPlayers.addAll(invitedPlayers());
        StringBuilder builder = new StringBuilder();
        tempPlayers.forEach((player -> {
            ManhuntGame.Team team = players.getOrDefault(player, ManhuntGame.Team.UNKNOWN);
            switch (team) {
                case RUNNER -> builder.append(ChatColor.GREEN + player.getDisplayName());
                case HUNTER -> builder.append(ChatColor.RED + player.getDisplayName());
                case SPECTATOR -> builder.append(ChatColor.GRAY + player.getDisplayName());
                default -> builder.append(ChatColor.DARK_GRAY + player.getDisplayName());
            }
            if (readyPlayers.contains(player)) {
                builder.append(ChatColor.DARK_GRAY + " : " + ChatColor.GREEN + "☑ ");
            } else {
                builder.append(ChatColor.DARK_GRAY + " : " + ChatColor.RED + "☒ ");
            }
        }));

        return builder.toString();
    }

    public void playerAcceptInvite(Player... addedPlayers) {
        System.out.println(players);
        for (Player player : addedPlayers) {
            players.put(player, ManhuntGame.Team.SPECTATOR);
        }
        update();
    }

    public Set<Player> getPlayers() {
        return players.keySet();
    }

    public ManhuntGame.Team getTeam(Player player) {
        return players.get(player);
    }

    /**
     * Puts a player on a team
     * This does no checks to see if the game state is correct
     * TODO: Check gamestates
     * @param player player joining the team
     * @param team team the player is joining
     */
    public void playerJoinTeam(Player player, ManhuntGame.Team team) {
        players.put(player, team);
        update();
    }

    public void playerReady(Player player) {
        if (readyPlayers.contains(player)) {
            readyPlayers.remove(player);
        } else {
            readyPlayers.add(player);
        }
        update();
    }
}
