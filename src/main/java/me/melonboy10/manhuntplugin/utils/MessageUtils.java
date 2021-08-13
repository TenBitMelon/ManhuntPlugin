package me.melonboy10.manhuntplugin.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MinecraftFont;

import java.util.Arrays;

public class MessageUtils {

    private static final int length = 235;

    public static void sendError(Player player, String content) {
        player.sendMessage(ChatColor.RED + "⚠ " + content + ChatColor.RED + " ⚠");
    }

    public static void sendLineBreak(Player player) {
        player.sendMessage(ChatColor.YELLOW + "+-----------------------------------------+");
    }

    public static void sendFormattedMessage(Player player, BaseComponent[] line) {
        sendFormattedMessage(player, flattenComponents(line));
    }

    public static void sendFormattedMessage(Player player, TextComponent line) {
        int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText()));
        if (width > length) {
            player.spigot().sendMessage(
                    new ComponentBuilder(".").color(ChatColor.DARK_GRAY.asBungee())
                            .append("|   ").color(ChatColor.YELLOW.asBungee())
                            .append(line)
                            .append("|").color(ChatColor.YELLOW.asBungee())
                            .create());
        } else {
            player.spigot().sendMessage(
                    new ComponentBuilder(".").color(ChatColor.DARK_GRAY.asBungee())
                            .append("|   ").color(ChatColor.YELLOW.asBungee())
                            .append(line)
                            .append(" ".repeat((length - width) / 4))
                            .append(".".repeat((length - width) % 4)).color(ChatColor.DARK_GRAY.asBungee())
                            .append("|").color(ChatColor.YELLOW.asBungee())
                            .create());
        }
    }

    public static void sendWrappedMessage(Player player, BaseComponent[] line) {
        sendWrappedMessage(player, flattenComponents(line));
    }

    public static void sendWrappedMessage(Player player, TextComponent line) {
        int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText()));
        if (width > length) {
            String[] split = line.getText().split(" ");
            String[] segments = split;
            String shrunkString = "";
            while (width > length) {
                segments = Arrays.copyOf(segments, segments.length - 1);
                StringBuilder builder = new StringBuilder();
                for (String segment : segments) {
                    builder.append(segment);
                }
                shrunkString = builder.toString();
                width = MinecraftFont.Font.getWidth(ChatColor.stripColor(shrunkString));
            }
            sendFormattedMessage(player, new TextComponent(shrunkString));
            StringBuilder builder = new StringBuilder();
            for (int i = segments.length; i < split.length; i++) {
                builder.append(split[i]);
            }
            sendWrappedMessage(player, new TextComponent(builder.toString()));
        } else {
            sendFormattedMessage(player, line);
        }
    }

    public static void sendBlankLine(Player player) {
        sendFormattedMessage(player, new TextComponent(""));
    }

    private static TextComponent flattenComponents(BaseComponent[] line) {
        TextComponent component = new TextComponent();
        for (BaseComponent baseComponent : line) {
            component.addExtra(baseComponent);
        }
        return component;
    }
}
