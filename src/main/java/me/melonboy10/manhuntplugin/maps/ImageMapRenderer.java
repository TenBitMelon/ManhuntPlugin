package me.melonboy10.manhuntplugin.maps;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class ImageMapRenderer extends MapRenderer {

    BufferedImage image;
    ManhuntPlugin plugin;

    public ImageMapRenderer(BufferedImage image, ManhuntPlugin plugin) {
        this.image = image;
        this.plugin = plugin;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (plugin.getServer().getWorld("world").getGameTime() % 40 == 0)
            canvas.drawImage(0, 0, image);
    }

}
