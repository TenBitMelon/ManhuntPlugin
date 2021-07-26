package me.melonboy10.manhuntplugin.menuSystem.menus;

import me.melonboy10.manhuntplugin.ManhuntGame;
import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.menuSystem.Menu;
import org.bukkit.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Random;

public class CreateGameMenu extends Menu {

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
                ChatColor.YELLOW + "Click to change!"
            ));

        inventory.setItem(13, makeItem(Material.FILLED_MAP, ChatColor.YELLOW + "World Preview",
                ""
            ));

        try {
            magic();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void magic() throws Exception {
//        Class<?> clazz = Class.forName("BiomeManager");
//        Object managerObj = clazz.newInstance();
//
//        Class<?> clazz2 = Class.forName("BlockPos");
//        Constructor<?> blockPosConstructor = clazz2.getConstructor();
//
//        Method getBiome = clazz.getMethod("getBiome");
//        System.out.println(getBiome.invoke(blockPosConstructor.newInstance(0, 0, 0)));
//
//        ChunkGenerator gen = new ChunkGenerator() {
//            BiomeGrid[][] biomes;
//
//            @Override
//            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
//                if (-8 < x && x < 8 && -8 < z && z < 8) {
//                    biomes[x][z] = biome;
//                    biome.getBiome(1, 1, 1);
//                }
//                return null;
//            }
//        };

        String url = "https://www.chunkbase.com/apps/biome-finder#" + new Random().nextLong();
        Document document = Jsoup.connect(url).get();
        Element canvas = document.body().select("#map-canvas").get(0);



    }
}
