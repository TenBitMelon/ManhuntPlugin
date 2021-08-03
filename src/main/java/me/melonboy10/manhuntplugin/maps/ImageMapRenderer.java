package me.melonboy10.manhuntplugin.maps;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.util.Map;

public class ImageMapRenderer extends MapRenderer {

    BufferedImage image;
    ManhuntPlugin plugin = ManhuntPlugin.plugin;
    boolean text = false;

    public ImageMapRenderer(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (!text) {
            canvas.drawImage(0, 0, image);
//            canvas.drawText(30, 48, MinecraftFont.Font, "Please Wait...");
            text = true;
        }
//        if (plugin.getServer().getWorld("world").getGameTime() % 40 == 0)
    }

}
