package me.melonboy10.manhuntplugin.menuSystem;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public abstract class Menu implements InventoryHolder {

    protected ManhuntPlugin plugin = ManhuntPlugin.plugin;
    protected Inventory inventory;
    protected ItemStack fillerGlass = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");

    public Menu() {}

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void clickEvent(InventoryClickEvent event);

    public abstract void closeMenu(InventoryCloseEvent event);

    public abstract void setMenuItems();

    public void open(Player player) {
        if (inventory == null) {
            inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
            this.setMenuItems();
        }
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setFillerGlass() {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, fillerGlass);
            }
        }
    }

    public static ItemStack makeItem(Material material, String name, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + name);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack makeItem(Material material, String name, int amount, String dataKey, String data, String... lore) {

        ItemStack item = new ItemStack(material);
        item.setAmount(amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + name);

        itemMeta.setLore(Arrays.asList(lore));

        itemMeta.getPersistentDataContainer().set(new NamespacedKey(ManhuntPlugin.getPlugin(ManhuntPlugin.class), dataKey), PersistentDataType.STRING , data);

        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack makeItem(Material material, int amount, String name, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + name);

        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        item.setAmount(amount);

        return item;
    }

}
