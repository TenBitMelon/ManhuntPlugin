package me.melonboy10.manhuntplugin.game;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.menuSystem.menus.TeamSelectMenu;
import me.melonboy10.manhuntplugin.menuSystem.menus.TeamSelectTextMenu;
import me.melonboy10.manhuntplugin.utils.MessageUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.*;

public class ManhuntGame {

    private static final ManhuntPlugin plugin = ManhuntPlugin.plugin;

    public enum Team {RUNNER, HUNTER, SPECTATOR, UNKNOWN}
    public enum GameState {GENERATING, HUNTER_COOLDOWN, PLAYING, GAME_OVER}
    private GameState gameState = GameState.GENERATING;
    private final ManhuntGameSettings settings;

    private final ManhuntGameWorldManager worldManager;
    private final int hunterCooldown;
    private int gameTime = 0;
    private final TeamSelectTextMenu teamTextMenu;
    private final TeamSelectMenu teamMenu;
    private BukkitTask gameRunnable;
    private final ItemStack mapItem;

    private final Player creator;
    private final ArrayList<Player> invitedPlayers = new ArrayList<>();
    private final HashMap<Player, Team> players = new HashMap<>();
    private final HashMap<Player, PlayerInventory> quitPlayerInventory = new HashMap<>();

    private final HashMap<Player, Location> quitPlayerLocation = new HashMap<>();
    private final HashMap<Player, Team> quitPlayerTeam = new HashMap<>();
    private final HashMap<Player, BukkitTask> quitPlayerTimer = new HashMap<>();
    public ManhuntGame(ManhuntGameSettings settings, Player creator, LinkedList<Player> invitedPlayers, ItemStack item) {
        this.settings = settings;
        hunterCooldown = settings.getHunterCooldown();
        mapItem = item;
        this.creator = creator;
        teamTextMenu = new TeamSelectTextMenu(this);
        teamMenu = new TeamSelectMenu(this);

        forceInvitePlayer(creator);
        playerAcceptInvite(creator);
        invitedPlayers.forEach(this::invitePlayer);

        ManhuntGameManager.add(this);

        World overworld = new WorldCreator(this.hashCode() + "-overworld")
            .seed(settings.getSeed())
            .environment(World.Environment.NORMAL)
            .generateStructures(true)
            .type(settings.getWorldType())
            .createWorld();
        if (overworld == null) {
            System.out.println("overworld null");
        } else {
            System.out.println("done overworld");
        }
        assert overworld != null;
        overworld.setDifficulty(settings.getDifficulty());
        setGameRules(overworld);

        World nether = new WorldCreator(this.hashCode() + "-nether")
            .seed(settings.getSeed())
            .environment(World.Environment.NETHER)
            .generateStructures(true)
            .type(settings.getWorldType())
            .createWorld();
        assert nether != null;
        nether.setDifficulty(settings.getDifficulty());
        setGameRules(nether);

        World end = new WorldCreator(this.hashCode() + "-end")
            .seed(settings.getSeed())
            .environment(World.Environment.THE_END)
            .generateStructures(true)
            .type(settings.getWorldType())
            .createWorld();
        assert end != null;
        end.setDifficulty(settings.getDifficulty());
        setGameRules(end);

        worldManager = new ManhuntGameWorldManager(overworld, nether, end);
    }

    private void setGameRules(World world) {
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        world.setGameRule(GameRule.DO_INSOMNIA, true);
        world.setGameRule(GameRule.DO_LIMITED_CRAFTING, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, true);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, true);
        world.setGameRule(GameRule.DO_TILE_DROPS, true);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, true);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(GameRule.DROWNING_DAMAGE, true);
        world.setGameRule(GameRule.FALL_DAMAGE, true);
        world.setGameRule(GameRule.FIRE_DAMAGE, true);
        world.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, true);
        world.setGameRule(GameRule.FREEZE_DAMAGE, true);
        world.setGameRule(GameRule.KEEP_INVENTORY, false);
        world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, true);
        world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 24);
        world.setGameRule(GameRule.MOB_GRIEFING, true);
        world.setGameRule(GameRule.NATURAL_REGENERATION, true);
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 100);
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 3);
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, false);
        world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, true);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, true);
        world.setGameRule(GameRule.SPAWN_RADIUS, 0);
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        world.setGameRule(GameRule.UNIVERSAL_ANGER, false);
    }

    public void forceInvitePlayer(Player creator) {
        invitedPlayers.add(creator);
    }

    /**
     * Used to invite a player and show invitee text
     * Checks if in a game and gives warning
     *
     * @param player Player being invited
     */
    public void invitePlayer(Player player) {
        if (!invitedPlayers.contains(player)) {
            invitedPlayers.add(player);
            teamTextMenu.update();
            MessageUtils.Builder builder = new MessageUtils.Builder(player)
                .lineBreak()
                .blankLine()
                .formattedMessage(new TextComponent(ChatColor.GREEN + "You have been invited to a new Manhunt!"))
                .blankLine();
            if (ManhuntGameManager.isPlayerInGame(player)) {
                builder.formattedMessage(new TextComponent(ChatColor.RED + "You are already in a game so to join you"))
                .formattedMessage(
                    new ComponentBuilder(ChatColor.RED + "need to leave your game by typing ")
                        .append("/leave")
                        .color(ChatColor.GOLD.asBungee())
                        .event(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Click to leave!")
                        ))
                        .event(new ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND, "/leave"
                        ))
                        .append(".")
                        .color(ChatColor.RED.asBungee())
                        .create()
                )
                .blankLine();
            }
            builder.formattedMessage(
                new ComponentBuilder(ChatColor.YELLOW + "You can join by typing or clicking").create()
            )
            .formattedMessage(new ComponentBuilder(ChatColor.GOLD + "/join " + this.hashCode())
                .event(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Click to join " + this.hashCode() + "!")
                ))
                .event(new ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND, "/join " + this.hashCode()
                ))
                .create()
            )
            .blankLine()
            .lineBreak();
        }
    }

    /**
     * Called when a player accepts the invite
     * Called only from the /join command
     *
     * PRIVATE:
     *    Player cannot join if not invited
     * SPECTATOR:
     *    Player can only join as spectator
     * PUBLIC:
     *    Anyone can join
     *
     * @param player Player joining
     */
    public void playerAcceptInvite(Player player) {
        if (ManhuntGameManager.isPlayerInGame(player)) {
            MessageUtils.sendError(player, "You are already in a game!");
        } else if (gameState.equals(GameState.GAME_OVER)) {
            MessageUtils.sendError(player, "This game is over!");
        } else {
            if (gameState.equals(GameState.GENERATING)) {
                teamTextMenu.playerAcceptInvite(player);
                ManhuntGameManager.playerJoinGame(player, this);
            } else if (quitPlayerTimer.containsKey(player)) {
                teleportIntoGame(player);
                ManhuntGameManager.playerJoinGame(player, this);
            } else if (settings.getPrivacy().equals(ManhuntGameSettings.Privacy.PUBLIC)) {
                teamMenu.open(player);
            } else if (settings.getPrivacy().equals(ManhuntGameSettings.Privacy.SPECTATOR_ONLY)) {
                players.put(player, Team.SPECTATOR);
                teleportIntoGame(player);
                ManhuntGameManager.playerJoinGame(player, this);
            } else if (invitedPlayers.contains(player) && settings.getPrivacy().equals(ManhuntGameSettings.Privacy.PRIVATE)) {
                teamMenu.open(player);
            }
        }
    }

    /**
     * Starts the game
     * Teleports all the players
     * Starts hunter cooldown
     *  -  after 1 second everyone cooldown
     *  -  needed for connection lag
     *
     *
     */
    public void startGame() {
        gameState = GameState.HUNTER_COOLDOWN;
        for (Player player : players.keySet()) {
            teleportIntoGame(player);
            player.sendTitle("", "", 0, 1, 0);
            Iterator<Advancement> iterator = Bukkit.advancementIterator();
            while (iterator.hasNext()) {
                AdvancementProgress progress = player.getAdvancementProgress(iterator.next());
                for (String criteria : progress.getAwardedCriteria())
                    progress.revokeCriteria(criteria);
            }
        }
        gameRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                gameTime++;
                players.forEach((player, team) -> {
                    if (team.equals(Team.HUNTER)) {
                        ItemStack compass = new ItemStack(Material.COMPASS);
                        CompassMeta itemMeta = (CompassMeta) compass.getItemMeta();
                        assert itemMeta != null;
                        itemMeta.setLodestoneTracked(false);
                        Player trackedPlayer = getClosestPlayer(player.getLocation());
                        if (trackedPlayer != null) {
                            itemMeta.setLodestone(trackedPlayer.getLocation());
                        }
                        compass.setItemMeta(itemMeta);

                        if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
                            player.getInventory().setItemInMainHand(compass);
                        } else if (player.getInventory().getItemInOffHand().getType().equals(Material.COMPASS)) {
                            player.getInventory().setItemInOffHand(compass);
                        } else if (player.getItemOnCursor().getType().equals(Material.COMPASS)) {
                            player.setItemOnCursor(compass);
                        } else if (!player.getInventory().contains(Material.COMPASS)) {
                            player.getInventory().addItem(compass);
                        }
                    }
                    if (gameTime < hunterCooldown) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Hunters released in " + (hunterCooldown - gameTime) + " seconds!"));
                    }
                });
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    @Nullable
    private Player getClosestPlayer(Location location) {
        Team team = Team.RUNNER;
        Player closest = null;
        double distance = Double.MAX_VALUE;
        try {
            for (Map.Entry<Player, Team> entry : players.entrySet()) {
                Player player = entry.getKey();
                Team team1 = entry.getValue();
                if (team1.equals(team) && !player.getGameMode().equals(GameMode.SPECTATOR)) {
                    double distance1 = location.distanceSquared(player.getLocation());
                    if (distance1 < distance) {
                        closest = player;
                        distance = distance1;
                    }
                }
            }
        } catch (Exception ignored) {}
        return closest;
    }

    /**
     * Called when teleporting a player into a game
     *
     * @param player Player being teleported
     */
    public void teleportIntoGame(Player player) {
        if (isWorldReady()) {
            if (players.containsKey(player) || quitPlayerTimer.containsKey(player)) {
                player.teleport(getOverworld().getSpawnLocation());
                player.setBedSpawnLocation(getOverworld().getSpawnLocation(), true);
                player.getInventory().clear();

                player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, hunterCooldown * 20 + 80, 250, true, false));
                if (quitPlayerTimer.containsKey(player)) {
                    players.put(player, quitPlayerTeam.get(player));
                    player.getInventory().setContents(quitPlayerInventory.get(player).getContents());
                    player.teleport(quitPlayerLocation.get(player));
                    quitPlayerTimer.get(player).cancel();
                    quitPlayerTimer.remove(player);
                    quitPlayerTeam.remove(player);
                    quitPlayerLocation.remove(player);
                    quitPlayerInventory.remove(player);
                }
                if (gameState.equals(GameState.HUNTER_COOLDOWN) && getTeam(player).equals(Team.HUNTER)) {
                    player.addPotionEffects(
                        List.of(new PotionEffect[]{
                            new PotionEffect(PotionEffectType.BLINDNESS, hunterCooldown * 20 + 20, 250, true, false),
                            new PotionEffect(PotionEffectType.SLOW_DIGGING, hunterCooldown * 20 + 20, 250, true, false),
                            new PotionEffect(PotionEffectType.SLOW, hunterCooldown * 20 + 20, 250, true, false),
                            new PotionEffect(PotionEffectType.JUMP, hunterCooldown * 20 + 20, 250, true, false),
                            new PotionEffect(PotionEffectType.WEAKNESS, hunterCooldown * 20 + 20, 250, true, false)
                        })
                    );
                }
                if (getTeam(player).equals(Team.SPECTATOR)) {
                    player.setGameMode(GameMode.SPECTATOR);
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                }
            }
        }
    }

    public void playerLeave(Player player) {
        Team team = getTeam(player);
        if (team.equals(Team.RUNNER) || team.equals(Team.HUNTER) && (!gameState.equals(GameState.GENERATING) && !gameState.equals(GameState.GAME_OVER))) {
            quitPlayerInventory.put(player, player.getInventory());
            quitPlayerLocation.put(player, player.getLocation());
            quitPlayerTeam.put(player, getTeam(player));

            quitPlayerTimer.put(player, new BukkitRunnable() {
                @Override
                public void run() {
                    quitPlayerTimer.remove(player);
                    quitPlayerInventory.remove(player);
                    quitPlayerLocation.remove(player);
                    quitPlayerTeam.remove(player);
                    checkWinConditions();
                }
            }.runTaskLater(plugin, 5 * 60 * 20));

            players.forEach((player1, team1) -> player1.sendMessage(ChatColor.RED + player.getName() + " has left the game! They have 5 minutes to rejoin!"));
        }
        players.remove(player);
        ManhuntGameManager.playerLeaveGame(player);
        ManhuntPlugin.sendPlayertoHub(player);
    }

    public void checkWinConditions() {
        if (!gameState.equals(GameState.GAME_OVER)) {
            if (isDragonDead()) {
                teamWins(Team.RUNNER);
            } else if (
                players.keySet().stream().noneMatch(player -> !player.getGameMode().equals(GameMode.SPECTATOR) && players.get(player).equals(Team.RUNNER)) &&
                quitPlayerTeam.keySet().stream().noneMatch(player -> !player.getGameMode().equals(GameMode.SPECTATOR) && players.get(player).equals(Team.RUNNER))
            ) {
                teamWins(Team.HUNTER);
            } else if (
                players.keySet().stream().noneMatch(player -> !player.getGameMode().equals(GameMode.SPECTATOR) && players.get(player).equals(Team.HUNTER)) &&
                quitPlayerTeam.keySet().stream().noneMatch(player -> !player.getGameMode().equals(GameMode.SPECTATOR) && players.get(player).equals(Team.HUNTER))
            ) {
                teamWins(Team.RUNNER);
            }
        }
    }

    public void teamWins(Team team) {
        gameState = GameState.GAME_OVER;
        players.forEach((player, team1) -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 100, true, false));
            player.setAllowFlight(true);
            player.setFlying(true);
        });

        switch (team) {
            case RUNNER -> players.forEach((player, team1) -> player.sendTitle((team1.equals(Team.RUNNER) ? ChatColor.GOLD + "YOU WIN" : ""), ChatColor.GREEN + "Runners Win", 0, 80, 0));
            case HUNTER -> players.forEach((player, team1) -> player.sendTitle((team1.equals(Team.HUNTER) ? ChatColor.GOLD + "YOU WIN" : ""), ChatColor.RED + "Hunters Win", 0, 80, 0));
            case UNKNOWN -> players.forEach((player, team1) -> player.sendTitle("", ChatColor.GRAY + "Tie", 0, 80, 0));
        }

        new BukkitRunnable() {
            int timeLeft = 30;
            @Override
            public void run() {
                players.forEach((player, team1) -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Teleporting back in " + timeLeft + ". Use /leave to leave early.")));
                timeLeft--;

                if (timeLeft < 0) {
                    players.forEach((player, team1) -> {
                        playerLeave(player);
                        ManhuntPlugin.sendPlayertoHub(player);
                    });
                    if (timeLeft < 10) {
                        shutDown();
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void playerDie(Player player) {
        Team team = getTeam(player);
        if (team == Team.RUNNER) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        checkWinConditions();
    }

    public void setTeam(Player player, Team team) {
        players.put(player, team);
    }

    /** GETTERS */

    public boolean isWorldReady() {
        return worldManager != null &&
            getOverworld() != null && Bukkit.getWorld(getOverworld().getUID()) != null &&
            getNether() != null && Bukkit.getWorld(getNether().getUID()) != null &&
            getEnd() != null && Bukkit.getWorld(getEnd().getUID()) != null;
    }

    public boolean isDragonDead() {
        if (getEnd().getEnderDragonBattle() != null) {
            return getEnd().getEnderDragonBattle().getEnderDragon() != null && getEnd().getEnderDragonBattle().getEnderDragon().isDead();
        }
        return false;
    }

    public Team getTeam(Player player) {
        if (gameState.equals(GameState.GENERATING)) {
            return teamTextMenu.getTeam(player);
        } else {
            return players.getOrDefault(player, quitPlayerTeam.getOrDefault(player, null));
        }
    }

    public ArrayList<Player> getInvitedPlayers() {
        return invitedPlayers;
    }

    public GameState getState() {
        return gameState;
    }

    public HashMap<Player, Team> getPlayers() {
        return players;
    }

    public TeamSelectTextMenu getTeamTextMenu() {
        return teamTextMenu;
    }

    public ManhuntGameSettings getSettings() {
        return settings;
    }

    public ItemStack getMapItem() {
        return mapItem;
    }

    public Player getCreator() {
        return creator;
    }

    public World getOverworld() {
        return worldManager.getOverworld();
    }

    public World getNether() {
        return worldManager.getNether();
    }

    public World getEnd() {
        return worldManager.getEnd();
    }

    public void shutDown() {
        //Delete folders\
        gameRunnable.cancel();
        worldManager.removeWorlds();
    }



    /*

    ⎛⎝(•ⱅ•)⎠⎞



     * Player Road Map - the player experience
     *
     * Player Joining to server
     * creates new game
     * options menu - can invite players
     * creates world
     * - new instance of game with settings (menu holds settings)
     * - Message sent to all members of party list - contains item preview of map world
     * - players can join teams
     * World gets generated an players get sent to world - blindness for all
     * grace period counts down - remove blindness from just runners
     * game runs normally
     * - hunter die and respawn
     * - all runners dies / leave and game over2
     * - enderdragon killed (detect advancement or mob kill) game over
     * - players leaves (freeze the game if possible) 5 mins to come back
     * - Game Over: teleport all players back after 15 seconds (players can leave during this stage)
     * - Save game to players history
     * - remove from current games and delete world
     *
     * Players joins server
     * opens current game menu
     * shows game all games in progress
     *
     */
    /*
     * invite command / button in menu
     *
     * Text team joining menu for players in joiningPlayers before the world is generated
     * Team menu GUI for after clicking in the all games menu
     * (runners, hunters, spectators, with options to join all)
     * invited but not joined players become dark gray
     * have a ready button for when players are ready (put a check or an x next to name)
     *
     * All games menu - for all currently playing games
     * Shows: Generating, Public, Private, Allowing spectators
     *
     * joiningPlayers - list of players to show the joining text
     * show message to all players saying (a game has been created click to join)
     *
     * Spectators - only for people to teleport in and currently spectating people
     * (added during team joining & people joining to spectate)
     *
     * Runners - a
     */
}
