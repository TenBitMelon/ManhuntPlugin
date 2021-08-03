package me.melonboy10.manhuntplugin;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import me.melonboy10.manhuntplugin.commands.CreateGameCommand;
import me.melonboy10.manhuntplugin.commands.JoinCommand;
import me.melonboy10.manhuntplugin.commands.LeaveCommand;
import me.melonboy10.manhuntplugin.commands.TeamsCommand;
import me.melonboy10.manhuntplugin.maps.MapListener;
import me.melonboy10.manhuntplugin.menuSystem.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class ManhuntPlugin extends JavaPlugin {

    public static ManhuntPlugin plugin;
    public static World hubWorld;

    @Override
    public void onEnable() {
        plugin = this;
        hubWorld = Bukkit.getWorld("world");

        CommandService drink = Drink.get(this);
        drink.register(new CreateGameCommand(), "create");
        drink.register(new TeamsCommand(), "teams");
        drink.register(new LeaveCommand(), "leave");
        drink.register(new JoinCommand(), "join");
        drink.registerCommands();

        registerListeners();
        setupFiles();

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
