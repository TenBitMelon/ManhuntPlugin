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
    //✇✆♹♸♿♾♽♼♻♺
    private static final String regex = "[⚠⎘♽☒☑⛏⚔⧈♻🛡_]";

    public static void sendError(Player player, String error) {
        player.sendMessage(ChatColor.RED + "⚠ " + error + ChatColor.RED + " ⚠");
    }

    public static void sendWarning(Player player, String warning) {
        player.sendMessage(ChatColor.YELLOW + "⚠ " + warning + ChatColor.YELLOW + " ⚠");
    }

    public static void sendSuccess(Player player, String message) {
        player.sendMessage(ChatColor.GREEN + "⚠ " + message + ChatColor.GREEN + " ⚠");
    }

    public static void sendLineBreak(Player player) {
        player.sendMessage(ChatColor.YELLOW + "+-----------------------------------------+");
    }

    public static void sendFormattedMessage(Player player, BaseComponent[] line) {
        sendFormattedMessage(player, flattenComponents(line));
    }

    public static void sendFormattedMessage(Player player, TextComponent line) {
        int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText())
            .replaceAll(regex, "...."));
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
        int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText())
            .replaceAll(regex, "...."));
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
                System.out.println(shrunkString);
                width = MinecraftFont.Font.getWidth(ChatColor.stripColor(shrunkString.replaceAll(regex, ".")));
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

    public static void sendEmptyLine(Player player) {
        player.sendMessage("");
    }

    public static class Builder {

        Player player;

        public Builder(Player player) {
            this.player = player;
        }

        public Builder blank() {
            sendBlankLine(player);
            return this;
        }

        public Builder lineBreak() {
            sendLineBreak(player);
            return this;
        }

        public Builder formatted(BaseComponent[] line) {
            sendFormattedMessage(player, line);
            return this;
        }

        public Builder formatted(String line) {
            sendFormattedMessage(player, new TextComponent(line));
            return this;
        }

        public Builder formatted(TextComponent line) {
            sendFormattedMessage(player, line);
            return this;
        }

        public Builder wrapped(BaseComponent[] line) {
            sendWrappedMessage(player, line);
            return this;
        }

        public Builder wrapped(TextComponent line) {
            sendWrappedMessage(player, line);
            return this;
        }

        public Builder keyVal(String key, Object value) {
            sendFormattedMessage(player, new TextComponent(ChatColor.AQUA + key + ": " + (value != null ? ChatColor.WHITE : ChatColor.GRAY) + value));
            return this;
        }
    }
}
