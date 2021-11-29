package me.melonboy10.manhuntplugin.menuSystem.menus;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameSettings;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static org.bukkit.ChatColor.*;

public class TeamSelectTextMenu {

    private final ManhuntGame game;
    // Contains all players that have joined the game
    private final HashMap<Player, ManhuntGame.Team> players;
    // Contains all players from "players" that have clicked ready
    private final ArrayList<Player> readyPlayers = new ArrayList<>();
    private final String serializedMap;
    private int countDown = -1;
    private BukkitTask runnable;
    private long timeCreated = System.currentTimeMillis();

    /**
     * This Object should be nulled once the game starts
     * @param game The MahuntGame using this
     */
    public TeamSelectTextMenu(ManhuntGame game) {
        this.game = game;
        this.players = game.getPlayers();

        StringBuilder builder = new StringBuilder("{display:{Name:'{\"text\":\"World Preview\",\"color\":\"yellow\",\"italic\":false}',Lore:[");
        List<String> lores = game.getMapItem().getItemMeta().getLore();
        lores.remove(0);
        lores.remove(lores.size() - 1);
        for (String lore : lores) {
            builder.append("'[");
            for (String character : lore.split("(?<=\\G...............)")) {
                String hex = character.substring(2, character.length() - 1).replaceAll(Character.toString(COLOR_CHAR), "");
                builder.append("{\"text\":\"█\",\"color\":\"#" + hex + "\",\"italic\":false},");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("]',");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]}}");

        this.serializedMap = builder.toString();
    }

    // Invited players
    private ArrayList<Player> invitedPlayers() {
        return game.getInvitedPlayers();
    }

    public void update() { // ☑☒⎆⎘♽
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
            MessageUtils.sendBlankLine(player);
            MessageUtils.sendFormattedMessage(player,
                new ComponentBuilder("Status").color(ChatColor.AQUA)
                    .append(":  ").color(ChatColor.DARK_GRAY)
                    .append("  ♻  ").color(game.isWorldReady() ? ChatColor.GREEN : ChatColor.GRAY)
                    .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_GRAY + "World Generation Done: " + BooleanUtils.toStringYesNo(game.isWorldReady()))
                    ))
                    .append("  " + (countDown > 0 ? countDown + "  " : "⚔  ")).color(readyPlayers.size() == players.size() ? ChatColor.GREEN : ChatColor.GRAY)
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_GRAY + "Everyone Ready: " + BooleanUtils.toStringYesNo(readyPlayers.size() == players.size()))
                    ))
                    .append("  ⛏  ").color(ChatColor.DARK_AQUA)
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
                                "}" +
                            "}"))
                    )).append("  🛡").color(ChatColor.DARK_AQUA)
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_ITEM, new Item(Material.MELON_SLICE.toString().toLowerCase(), 1, ItemTag.ofNbt(
                            serializedMap
                        ))
                    ))
                    .append("   |").color(ChatColor.DARK_GRAY)
                    .append(readyPlayers.contains(player) ? ChatColor.GREEN + "   ☑   ": ChatColor.RED + "   ☒   ")
                    .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(readyPlayers.contains(player) ? ChatColor.RED + "Click to Unready!" : ChatColor.GREEN + "Click to Ready!")
                    ))
                    .event(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND, "/ready"
                    ))
                    .create()
            );
            if ( countDown > 0) {
                player.sendTitle("", ChatColor.GREEN + ">" + countDown + "<", 0, 20, 0);
            }
            MessageUtils.sendBlankLine(player);
            MessageUtils.sendWrappedMessage(player,
                getPlayerList()
            );
            MessageUtils.sendBlankLine(player);
            MessageUtils.sendFormattedMessage(player, new ComponentBuilder()
                .append("Runner")
                    .color(GREEN.asBungee())
                    .event(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND, "/teams runner"
                    ))
                    .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Click to join the Runners!")
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
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Click to join the Hunters!")
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
                        HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Click to join the Spectators!")
                    ))
                .append(" : ")
                    .color(DARK_GRAY.asBungee())
                .append(players.values().stream().filter(team -> team.equals(ManhuntGame.Team.SPECTATOR)).count() + "   ")
                    .color(GRAY.asBungee())
                .create()
            );
            MessageUtils.sendBlankLine(player);
            MessageUtils.sendLineBreak(player);
            if (runnable == null && checkReady()) {
                beginCountDown();
            }
        }
    }

    private void beginCountDown() {
        countDown = 10;

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (checkReady()) {
                    if (countDown < 0) {
                        game.startGame();
                        cancel();
                    } else {
                        countDown--;
                        update();
                    }
                } else {
                    cancel();
                }
            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                countDown = -1;
                runnable = null;
                super.cancel();
            }
        }.runTaskTimer(ManhuntPlugin.plugin, 0, 20);
    }

    private ArrayList<TextComponent> getPlayerList() {
        ArrayList<TextComponent> returnList = new ArrayList<>();

        Set<Player> tempPlayers = new LinkedHashSet<>();
        tempPlayers.addAll(players.keySet());
        tempPlayers.addAll(invitedPlayers());
        tempPlayers.forEach((player -> {
            StringBuilder builder = new StringBuilder();
            boolean invitee = false;
            ManhuntGame.Team team = players.getOrDefault(player, ManhuntGame.Team.UNKNOWN);
            switch (team) {
                case RUNNER -> builder.append(ChatColor.GREEN + player.getDisplayName());
                case HUNTER -> builder.append(ChatColor.RED + player.getDisplayName());
                case SPECTATOR -> builder.append(ChatColor.GRAY + player.getDisplayName());
                default -> {
                    builder.append(ChatColor.DARK_GRAY + player.getDisplayName());
                    invitee = true;
                }
            }
            if (!invitee) {
                if (readyPlayers.contains(player)) {
                    builder.append(ChatColor.DARK_GRAY + " : " + ChatColor.GREEN + "☑ ");
                } else {
                    builder.append(ChatColor.DARK_GRAY + " : " + ChatColor.RED + "☒ ");
                }
            }

            returnList.add(new TextComponent(builder.toString()));
        }));
        return returnList;
    }

    public void playerAcceptInvite(Player... addedPlayers) {
        for (Player player : addedPlayers) {
            players.put(player, ManhuntGame.Team.SPECTATOR);
            if (!invitedPlayers().contains(player) && game.getSettings().getPrivacy().equals(ManhuntGameSettings.Privacy.SPECTATOR_ONLY)) {
                readyPlayers.add(player);
            }
        }
        update();
    }

    public Set<Player> getPlayers() {
        return players.keySet();
    }

    public ManhuntGame.Team getTeam(Player player) {
        return players.get(player);
    }

    public long getTimeInMenu() {
        return System.currentTimeMillis() - timeCreated;
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

    private boolean checkReady() {
        boolean simpleCheck = game.isWorldReady() && readyPlayers.size() == players.size();
        boolean enoughRunners = players.containsValue(ManhuntGame.Team.HUNTER) && players.containsValue(ManhuntGame.Team.RUNNER);
        return  simpleCheck && enoughRunners;
    }
}
