package me.melonboy10.manhuntplugin.maps;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class ImageMapRenderer extends MapRenderer {

    final BufferedImage image;
    boolean text = false;

    public ImageMapRenderer(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        if (!text) {
            canvas.drawImage(0, 0, image);
//            canvas.drawText(30, 48, MinecraftFont.Font, "Please Wait...");
            text = true;
        }
//        if (plugin.getServer().getWorld("world").getGameTime() % 40 == 0)
    }

}
