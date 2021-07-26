package me.melonboy10.manhuntplugin.menuSystem.menus;

import me.melonboy10.manhuntplugin.ManhuntGame;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.maps.ImageMapRenderer;
import me.melonboy10.manhuntplugin.maps.MapListener;
import me.melonboy10.manhuntplugin.menuSystem.Menu;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateGameMenu extends Menu {

    private long seed;
    private BufferedImage mapImage;

    public CreateGameMenu(ManhuntPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getMenuName() {
        return "Create a Manhunt World";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void clickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 10 -> {
                ManhuntGame.difficulty = getNextDifficulty(ManhuntGame.difficulty);
                setMenuItems();
            }
            case 13 -> {
                ItemStack map = new ItemStack(Material.FILLED_MAP);
                MapMeta mapMeta = (MapMeta) map.getItemMeta();

                mapMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "viewing-map"), PersistentDataType.INTEGER, 1);

                MapView view = Bukkit.createMap(player.getWorld());
                view.setTrackingPosition(false);
                view.setUnlimitedTracking(false);
                view.setLocked(true);
                view.getRenderers().clear();
                view.addRenderer(new ImageMapRenderer(mapImage, plugin));
                mapMeta.setMapView(view);

                map.setItemMeta(mapMeta);
                player.getInventory().setItemInMainHand(map);
                player.closeInventory();
                MapListener.addPlayer(player);
            }
        }
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        inventory.setItem(10, makeItem(Material.GRASS_BLOCK, ChatColor.YELLOW + "World Generation Type",
                ChatColor.DARK_GRAY + "Generation",
                "",
                ChatColor.AQUA + "Selected World Type:",
                ChatColor.GRAY + " - " + (ManhuntGame.worldType == WorldType.NORMAL ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Default",
                ChatColor.GRAY + " - " + (ManhuntGame.worldType == WorldType.LARGE_BIOMES ?  ChatColor.DARK_AQUA : ChatColor.GRAY) + "Large Biomes",
                ChatColor.GRAY + " - " + (ManhuntGame.worldType == WorldType.AMPLIFIED ?  ChatColor.DARK_AQUA : ChatColor.GRAY) + "Amplified",
                "",
                ChatColor.YELLOW + "Click to toggle!"
            ));

        inventory.setItem(11, makeItem(switch (ManhuntGame.difficulty) {
            case EASY -> Material.GOLDEN_SWORD;
            case NORMAL -> Material.IRON_SWORD;
            case HARD -> Material.DIAMOND_SWORD;
            default -> Material.RED_TULIP;
        },
                ChatColor.YELLOW + "Difficulty",
                ChatColor.DARK_GRAY + "Gameplay",
                "",
                ChatColor.AQUA + "Selected Difficulty:",
                ChatColor.GRAY + " - " + (ManhuntGame.difficulty == Difficulty.EASY ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Easy",
                ChatColor.GRAY + " - " + (ManhuntGame.difficulty == Difficulty.NORMAL ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Normal",
                ChatColor.GRAY + " - " + (ManhuntGame.difficulty == Difficulty.HARD ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Hard",
                ChatColor.GRAY + " - " + (ManhuntGame.difficulty == Difficulty.PEACEFUL ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Peaceful",
                "",
                ChatColor.YELLOW + "Click to toggle!"
            ));

        inventory.setItem(12, makeItem(Material.MAP, ChatColor.YELLOW + "Seed",
                ChatColor.DARK_GRAY + "Generation",
                "",
                ChatColor.AQUA + "Set Seed:",
                ChatColor.GRAY + "" + ManhuntGame.seed,
                "",
                ChatColor.YELLOW + "Click to change!",
                ChatColor.YELLOW + "Right-Click to randomize!"
            ));

        if (this.seed != ManhuntGame.seed) {
            inventory.setItem(13, createMapItem());
        }

        setFillerGlass();
        seed = ManhuntGame.seed;
    }

    private WorldType getNextWorldType(WorldType type) {
        switch (type) {
            case NORMAL -> {
                return WorldType.LARGE_BIOMES;
            }
            case LARGE_BIOMES -> {
                return WorldType.AMPLIFIED;
            }
            default -> {
                return WorldType.NORMAL;
            }
        }
    }

    private Difficulty getNextDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> {
                return Difficulty.NORMAL;
            }
            case NORMAL -> {
                return Difficulty.HARD;
            }
            case HARD -> {
                return Difficulty.PEACEFUL;
            }
            default -> {
                return Difficulty.EASY;
            }
        }
    }

    private ItemStack createMapItem() {

        File finder = new File(plugin.getDataFolder().getPath() + "/generateMapPreview");
        File rgbValues = new File(plugin.getDataFolder().getPath() + "/image.txt");

        try {
            new ProcessBuilder(finder.getAbsolutePath(), String.valueOf(ManhuntGame.seed))
                    .redirectOutput(rgbValues)
                    .start()
                    .waitFor(10, TimeUnit.SECONDS);

            List<Color> rgbArray = new ArrayList<>();

            for (String line : Files.readAllLines(rgbValues.toPath())) {
                String[] data = line.split(",");
                rgbArray.add(new Color(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])));
            }

            BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < rgbArray.size(); i++) {
                image.setRGB(i % 128, i / 128 % 128, rgbArray.get(i).getRGB());
            }

            mapImage = image;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }






        BufferedImage itemImage = mapImage.getSubimage(16, 16, 64, 64);
        try {//                                                ManhuntData        Plugins        Server      ""
            ImageIO.write(itemImage, "PNG", new File(plugin.getDataFolder().getParentFile().getAbsoluteFile().getParentFile().toPath() + "/server-icon.png"));
            Bukkit.getServer().loadServerIcon(itemImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedImage itemImage16 = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        itemImage16.getGraphics().drawImage(itemImage, 0, 0, 16, 16, null);

        ArrayList<Color> rgb = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                rgb.add(new Color(itemImage16.getRGB(i, j)));
            }
        }

        StringBuilder builder = new StringBuilder();
        for (Color color : rgb) {
            builder.append(net.md_5.bungee.api.ChatColor.of(color) + "");
        }

        return makeItem(Material.FILLED_MAP, ChatColor.YELLOW + "World Preview",
                "World",
                "",
                "",
                "",
                ChatColor.YELLOW + "Click to enlarge!"
        );
    }
}
