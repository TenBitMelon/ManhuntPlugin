package me.melonboy10.manhuntplugin.game;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class ManhuntGameWorldManager {

    private final World overworld;
    private final World nether;
    private final World end;
    private final ManhuntPlugin plugin = ManhuntPlugin.plugin;

    public ManhuntGameWorldManager(World overworld, World nether, World end) {
        this.overworld = overworld;
        this.nether = nether;
        this.end = end;
    }

    public ManhuntGameWorldManager() {
        overworld = null;
        nether = null;
        end = null;
    }

    public void removeWorlds() {
        Bukkit.unloadWorld(overworld, false);
        Bukkit.unloadWorld(nether, false);
        Bukkit.unloadWorld(end, false);

        Bukkit.getServer().getWorlds().remove(overworld);
        Bukkit.getServer().getWorlds().remove(nether);
        Bukkit.getServer().getWorlds().remove(end);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!deleteDirectory(overworld.getWorldFolder())) System.out.println("Overworld was not deleted | " + this.hashCode());
                if (!deleteDirectory(nether.getWorldFolder())) System.out.println("Nether was not deleted | " + this.hashCode());
                if (!deleteDirectory(end.getWorldFolder())) System.out.println("End was not deleted | " + this.hashCode());
            }
        }.runTaskLater(plugin, 40);
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public World getOverworld() {
        return overworld;
    }

    public World getNether() {
        return nether;
    }

    public World getEnd() {
        return end;
    }
}
