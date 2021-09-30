package me.melonboy10.manhuntplugin;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import me.melonboy10.manhuntplugin.commands.*;
import me.melonboy10.manhuntplugin.maps.MapListener;
import me.melonboy10.manhuntplugin.menuSystem.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Structure;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.management.OperatingSystemMXBean;
import java.util.logging.Level;

public final class ManhuntPlugin extends JavaPlugin {

    public static ManhuntPlugin plugin;
    public static World hubWorld;

    public enum OS {WINDOWS, MAC, LINUS, CENTOS};
    public static OS operatingSystem = null;

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
        drink.registerCommands();

        registerListeners();
        setupFiles();

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
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.getServer().getPluginManager().registerEvents(new MapListener(), this);
    }

    private void setupFiles() {
        this.getDataFolder().mkdir();
    }

}
