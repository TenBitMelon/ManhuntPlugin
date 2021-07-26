package me.melonboy10.manhuntplugin;

import me.melonboy10.manhuntplugin.commands.CreateGameCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ManhuntPlugin extends JavaPlugin {

    public static ManhuntPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        registerCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        this.getCommand("create").setExecutor(new CreateGameCommand());
    }

}
