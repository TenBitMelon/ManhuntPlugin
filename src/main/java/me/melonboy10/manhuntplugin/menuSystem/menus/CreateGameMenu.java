package me.melonboy10.manhuntplugin.menuSystem.menus;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.commands.CreateGameCommand;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import me.melonboy10.manhuntplugin.game.ManhuntGameSettings;
import me.melonboy10.manhuntplugin.maps.ImageMapRenderer;
import me.melonboy10.manhuntplugin.maps.MapListener;
import me.melonboy10.manhuntplugin.menuSystem.Menu;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static me.melonboy10.manhuntplugin.game.ManhuntGameSettings.Privacy;

public class CreateGameMenu extends Menu {

    // Private or public game setting or only spectators
    // double click to make world
    private BufferedImage mapImage;
    private final ManhuntGameSettings settings;
    private LinkedList<Player> invitedPlayers = new LinkedList<>();
    private boolean worldChange = false;

    public CreateGameMenu(String seed) {
        if (seed.isBlank())
            settings = new ManhuntGameSettings();
        else
            settings = new ManhuntGameSettings(seed);
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
//            Difficulty
            case 10 -> {
                settings.setDifficulty(getNextDifficulty(settings.getDifficulty()));
                setMenuItems();
            }
//            World Type
            case 11 -> {
                settings.setWorldType(getNextWorldType(settings.getWorldType()));
                worldChange = true;
                setMenuItems();
            }
//            Seed
            case 12 -> {
                switch (event.getClick()) {
                    case LEFT, SHIFT_LEFT, MIDDLE, DROP, CONTROL_DROP, CREATIVE, SWAP_OFFHAND -> {
                        AnvilGUI.Builder builder = new AnvilGUI.Builder();
                        builder
                                .itemLeft(makeItem(Material.PAPER, "", "", ChatColor.RED + "Click to return!"))
                                .text("Enter Seed")
                                .title("Enter the world seed!")
                                .plugin(plugin)
                                .onClose((player1 -> {
                                    player1.openInventory(this.inventory);
                                }))
                                .onLeftInputClick((player1 -> {
                                    player1.openInventory(this.inventory);
                                }))
                                .onComplete((player1, text) -> {
                                    try {
                                        settings.setSeed(Long.parseLong(text));
                                    } catch (Exception e) {
                                        settings.setSeed(text.hashCode());
                                    }
//                                    this.setMenuItems();
                                    return AnvilGUI.Response.openInventory(this.inventory);
                                })
                                .open(player);
                    }
                    case RIGHT, SHIFT_RIGHT -> {
                        settings.setSeed(new Random().nextLong());
                    }
                }
                worldChange = true;
                setMenuItems();
            }
//            World Preview
            case 13 -> {
                ItemStack map = new ItemStack(Material.FILLED_MAP);
                MapMeta mapMeta = (MapMeta) map.getItemMeta();

                mapMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "viewing-map"), PersistentDataType.INTEGER, 1);

                MapView view = Bukkit.getMap(0);
                if (view == null)
                    view = Bukkit.createMap(Bukkit.getWorld("world"));
                view.setTrackingPosition(false);
                view.setUnlimitedTracking(false);
                view.setLocked(true);
                view.getRenderers().clear();
                view.addRenderer(new ImageMapRenderer(mapImage));
                mapMeta.setMapView(view);

                map.setItemMeta(mapMeta);
                player.getInventory().setItemInMainHand(map);
                player.closeInventory();
                MapListener.addPlayer(player);
            }
//            Privacy
            case 14 -> {
                settings.setPrivacy(getNextPrivacy(settings.getPrivacy()));
                setMenuItems();
            }
//            Hunter Delay
            case 15 -> {
                switch (event.getClick()) {
                    case LEFT, SHIFT_LEFT, MIDDLE, DROP, SWAP_OFFHAND, CONTROL_DROP -> {
                        settings.setHunterCooldown(settings.getHunterCooldown() + 1);
                    }
                    case RIGHT, SHIFT_RIGHT -> {
                        settings.setHunterCooldown(Math.max(settings.getHunterCooldown() - 1, 0));
                    }
                }
                setMenuItems();
            }
//            Invites
            case 16 -> {
                AnvilGUI.Builder builder = new AnvilGUI.Builder();
                builder
                        .itemLeft(makeItem(Material.NAME_TAG, "", "", ChatColor.RED + "Click to close!"))
                        .text("Enter Player Name")
                        .title("Enter a player!")
                        .plugin(plugin)
                        .onClose((player1 -> {
                            player1.openInventory(this.inventory);
                        }))
                        .onLeftInputClick((player1 -> {
                            player1.openInventory(this.inventory);
                        }))
                        .onComplete((player1, text) -> {
                            Player player2 = Bukkit.getPlayerExact(text);
                            if (player2 != null && player2.isOnline()) {
                                if (player2 != player1) {
                                    invitedPlayers.add(player2);
                                    this.setMenuItems();
                                    return AnvilGUI.Response.openInventory(this.inventory);
                                } else {
                                    return AnvilGUI.Response.text("You cannot invite yourself!");
                                }
                            } else {
                                return AnvilGUI.Response.text("That player doesn't exist or is offline!");
                            }
                        })
                        .open(player);
            }
//            Create button
            case 22 -> {
                if (event.getClick().equals(ClickType.DOUBLE_CLICK)) {
                    CreateGameCommand.playerMenuMap.put(player, null);
                    ManhuntGameManager.create(settings, new ArrayList<>(invitedPlayers), player);
                }
            }
        }
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {

        inventory.setItem(10, makeItem(switch (settings.getDifficulty()) {
                    case EASY -> Material.GOLDEN_SWORD;
                    case NORMAL -> Material.IRON_SWORD;
                    case HARD -> Material.DIAMOND_SWORD;
                    default -> Material.RED_TULIP;
                },
                ChatColor.YELLOW + "Difficulty",
                ChatColor.DARK_GRAY + "Gameplay",
                "",
                ChatColor.AQUA + "Selected Difficulty:",
                ChatColor.GRAY + " - " + (settings.getDifficulty() == Difficulty.EASY ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Easy",
                ChatColor.GRAY + " - " + (settings.getDifficulty() == Difficulty.NORMAL ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Normal",
                ChatColor.GRAY + " - " + (settings.getDifficulty() == Difficulty.HARD ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Hard",
                ChatColor.GRAY + " - " + (settings.getDifficulty() == Difficulty.PEACEFUL ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Peaceful",
                "",
                ChatColor.YELLOW + "Click to toggle!"
            ));

        inventory.setItem(11, makeItem(switch (settings.getWorldType()) {
                    case LARGE_BIOMES -> Material.PODZOL;
                    case AMPLIFIED -> Material.POINTED_DRIPSTONE;
                    default -> Material.GRASS_BLOCK;
                },
                ChatColor.YELLOW + "World Generation Type",
                ChatColor.DARK_GRAY + "Generation",
                "",
                ChatColor.AQUA + "Selected World Type:",
                ChatColor.GRAY + " - " + (settings.getWorldType() == WorldType.NORMAL ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Default",
                ChatColor.GRAY + " - " + (settings.getWorldType() == WorldType.LARGE_BIOMES ?  ChatColor.DARK_AQUA : ChatColor.GRAY) + "Large Biomes",
                ChatColor.GRAY + " - " + (settings.getWorldType() == WorldType.AMPLIFIED ?  ChatColor.DARK_AQUA : ChatColor.GRAY) + "Amplified",
                "",
                ChatColor.YELLOW + "Click to toggle!"
        ));

        inventory.setItem(12, makeItem(Material.MAP,
                ChatColor.YELLOW + "Seed",
                ChatColor.DARK_GRAY + "Generation",
                "",
                ChatColor.AQUA + "Set Seed:",
                ChatColor.GRAY + "" + settings.getSeed(),
                "",
                ChatColor.YELLOW + "Click to change!",
                ChatColor.YELLOW + "Right-Click to randomize!"
            ));

        if (worldChange) {
            inventory.setItem(13, createMapItem());
        }

        inventory.setItem(14, makeItem(switch (settings.getPrivacy()) {
                    case PRIVATE -> Material.ENDER_PEARL;
                    case SPECTATOR_ONLY -> Material.ENDER_EYE;
                    default -> Material.SLIME_BALL;
                },
                ChatColor.YELLOW + "Privacy",
                ChatColor.DARK_GRAY + "Gameplay",
                "",
                ChatColor.AQUA + "Selected Privacy Setting:",
                ChatColor.GRAY + " - " + (settings.getPrivacy() == Privacy.PRIVATE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Private",
                ChatColor.GRAY + " - " + (settings.getPrivacy() == Privacy.SPECTATOR_ONLY ?  ChatColor.DARK_AQUA : ChatColor.GRAY) + "Spectator Only",
                ChatColor.GRAY + " - " + (settings.getPrivacy() == Privacy.PUBLIC ?  ChatColor.DARK_AQUA : ChatColor.GRAY) + "Public",
                "",
                ChatColor.YELLOW + "Click to toggle!"
            ));

        inventory.setItem(15, makeItem(Material.CLOCK,
                ChatColor.YELLOW + "Hunter Cooldown",
                ChatColor.DARK_GRAY + "Gameplay",
                "",
                ChatColor.AQUA + "Set Hunter Cooldown:",
                ChatColor.GRAY + "" + settings.getHunterCooldown(),
                "",
                ChatColor.YELLOW + "Click to increase!",
                ChatColor.YELLOW + "Right-Click to decrease!"
            ));

        ItemStack inviteItem = makeItem(Material.NAME_TAG,
                ChatColor.YELLOW + "Invite Players",
                ChatColor.DARK_GRAY + "Gameplay",
                "",
                ChatColor.AQUA + "Invited Players:"
        );
        ItemMeta inviteItemMeta = inviteItem.getItemMeta();
        List<String> lore = inviteItemMeta.getLore();
        if (invitedPlayers.size() > 0) {
            lore.addAll(invitedPlayers.stream().map(p -> ChatColor.GRAY + " - " + p.getName()).toList());
        } else {
            lore.add(ChatColor.GRAY + "No-one has been invited!");
        }
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to add players!");
        lore.add(ChatColor.YELLOW + "Right-Click to remove last player!");
        inviteItemMeta.setLore(lore);
        inviteItem.setItemMeta(inviteItemMeta);

        inventory.setItem(16, inviteItem);

        inventory.setItem(22, makeItem(Material.LIME_TERRACOTTA,
                ChatColor.GREEN + "Create World",
                ChatColor.YELLOW + "Double-Click to create!"));

        setFillerGlass();
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

    private Privacy getNextPrivacy(Privacy privacy) {
        switch (privacy) {
            case PRIVATE -> {
                return Privacy.SPECTATOR_ONLY;
            }
            case SPECTATOR_ONLY -> {
                return Privacy.PUBLIC;
            }
            default -> {
                return Privacy.PRIVATE;
            }
        }
    }

    private ItemStack createMapItem() {
        File finder = new File(plugin.getDataFolder().getPath() + "/generateMapPreview");
        File rgbValues = new File(plugin.getDataFolder().getPath() + "/image.txt");

        try {
            new ProcessBuilder(finder.getAbsolutePath(),
                        String.valueOf(settings.getSeed()),
                        settings.getWorldType().equals(WorldType.LARGE_BIOMES) ? "--largeBiomes" : ""
                    )
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

        BufferedImage itemImage = mapImage.getSubimage(32, 32, 64, 64);
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
                rgb.add(new Color(itemImage16.getRGB(j, i)));
            }
        }

        StringBuilder builder = new StringBuilder();
        ArrayList<String> worldLore = new ArrayList<>();

        for (int i = 0, rgbSize = rgb.size(); i < rgbSize; i++) {
            if (i % 16 == 0) {
                worldLore.add(builder.toString());
                builder = new StringBuilder();
            }
            Color color = rgb.get(i);
            builder.append(net.md_5.bungee.api.ChatColor.of(color) + "█");
        }

        ItemStack item = new ItemStack(Material.FILLED_MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "World Preview");

        worldLore.set(0, ChatColor.DARK_GRAY + "Generation");
        worldLore.add(ChatColor.YELLOW + "Click to enlarge!");
        meta.setLore(worldLore);
        item.setItemMeta(meta);

        return item;
    }
}
