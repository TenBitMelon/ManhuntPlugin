package me.melonboy10.manhuntplugin;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import me.melonboy10.manhuntplugin.commands.CreateGameCommand;
import me.melonboy10.manhuntplugin.commands.TeamsCommand;
import me.melonboy10.manhuntplugin.maps.MapListener;
import me.melonboy10.manhuntplugin.menuSystem.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ManhuntPlugin extends JavaPlugin {

    public static ManhuntPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        CommandService drink = Drink.get(this);
        drink.register(new CreateGameCommand(this), "create", "game");
        drink.register(new TeamsCommand(), "teams");
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
        this.getServer().getPluginManager().registerEvents(new MapListener(this), this);
    }

    private void setupFiles() {
        this.getDataFolder().mkdir();
    }

}
