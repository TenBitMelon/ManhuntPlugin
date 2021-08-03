package me.melonboy10.manhuntplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void sendError(Player player, String content) {
        if (!content.endsWith("!")) content += ChatColor.RED + "!";

        player.sendMessage(ChatColor.RED + "⚠ " + content + ChatColor.RED + " ⚠");
    }
}
