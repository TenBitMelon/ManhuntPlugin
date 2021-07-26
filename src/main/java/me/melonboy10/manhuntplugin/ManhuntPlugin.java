package me.melonboy10.manhuntplugin;

import me.melonboy10.manhuntplugin.commands.CreateGameCommand;
import me.melonboy10.manhuntplugin.maps.MapListener;
import me.melonboy10.manhuntplugin.menuSystem.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ManhuntPlugin extends JavaPlugin {

    public static ManhuntPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        registerCommands();
        registerListeners();
        setupFiles();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        this.getCommand("create").setExecutor(new CreateGameCommand());
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.getServer().getPluginManager().registerEvents(new MapListener(this), this);
    }

    private void setupFiles() {
        this.getDataFolder().mkdir();
    }

}
