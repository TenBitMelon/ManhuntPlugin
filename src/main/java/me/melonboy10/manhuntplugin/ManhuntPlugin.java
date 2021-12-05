package me.melonboy10.manhuntplugin;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import me.melonboy10.manhuntplugin.commands.*;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.listeners.*;
import me.melonboy10.manhuntplugin.maps.MapListener;
import me.melonboy10.manhuntplugin.menuSystem.Menu;
import me.melonboy10.manhuntplugin.menuSystem.MenuListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ManhuntPlugin extends JavaPlugin {

    public static ManhuntPlugin plugin;
    public static World hubWorld;

    public enum OS {WINDOWS, MAC, LINUS, CENTOS}

    public static OS operatingSystem = null;

    public static ItemStack currentGamesItem;
    public static ItemStack gameHistoryItem;
    public static ItemStack createGameItem;

    @Override
    public void onEnable() {
        plugin = this;
        hubWorld = Bukkit.getWorld("world");

        CommandService drink = Drink.get(this);
        drink.register(new CreateGameCommand(), "create");
        drink.register(new DebugCommand(), "debugplayer");
        drink.register(new InviteCommand(), "invite");
        drink.register(new JoinCommand(), "join");
        drink.register(new LeaveCommand(), "leave");
        drink.register(new ReadyCommand(), "ready");
        drink.register(new TeamsCommand(), "teams");
        drink.register(new SaveSeedCommand(), "saveseed");
        drink.registerCommands();

        registerListeners();
        setupFiles();
        createItems();

        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("OS is " + os + ".");
        if (os.contains("window")) {
            operatingSystem = OS.WINDOWS;
        } else if (os.contains("mac")) {
            operatingSystem = OS.MAC;
        } else if (os.contains("linux")) {
            operatingSystem = OS.LINUS;
        } else if (os.contains("cent")) {
            operatingSystem = OS.CENTOS;
        } else {
            System.out.println(ChatColor.RED + "No OS detected! You are using " + os + ", and it is not supported!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ManhuntGameManager.clearGames();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.getServer().getPluginManager().registerEvents(new MapListener(), this);
        this.getServer().getPluginManager().registerEvents(new WorldLoadListener(), this);
        this.getServer().getPluginManager().registerEvents(new EnterPortalListener(), this);
        this.getServer().getPluginManager().registerEvents(new DropItemListener(), this);
        this.getServer().getPluginManager().registerEvents(new HubItemListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new JoinServerListener(), this);
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    private void setupFiles() {
        this.getDataFolder().mkdir();
    }

    private void createItems() {
        currentGamesItem = Menu.makeItem(
            Material.COMPASS,
            ChatColor.YELLOW + "Games in Progress"
        );

        gameHistoryItem = Menu.makeItem(
            Material.WRITABLE_BOOK,
            ChatColor.YELLOW + "Your Game History"
        );

        createGameItem = Menu.makeItem(
            Material.GOLDEN_SWORD,
            ChatColor.YELLOW + "Create Game"
        );
    }

    public static void sendPlayertoHub(Player player) {
        if (!ManhuntGameManager.isPlayerInGame(player)) {
            player.teleport(ManhuntPlugin.hubWorld.getSpawnLocation().clone().add(0.5, 0, 0.5));
            player.setBedSpawnLocation(ManhuntPlugin.hubWorld.getSpawnLocation().clone().add(0.5, 0, 0.5), true);
            player.setGameMode(GameMode.ADVENTURE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 100, true, false));
            player.setAllowFlight(false);
            player.setLevel(0);
            player.setExp(0);
            player.setCompassTarget(hubWorld.getSpawnLocation());
//            player.

            player.getInventory().clear();
            player.getInventory().setItem(0, currentGamesItem);
            player.getInventory().setItem(7, gameHistoryItem);
            player.getInventory().setItem(8, createGameItem);
        }
    }

}
