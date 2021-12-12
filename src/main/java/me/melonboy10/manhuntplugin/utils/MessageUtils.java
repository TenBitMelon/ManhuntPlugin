package me.melonboy10.manhuntplugin.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MinecraftFont;

import java.util.ArrayList;

public class MessageUtils {

    private static final int MAX_LENGTH = 235;
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

    private static void sendLineBreak(Player player) {
        player.sendMessage(ChatColor.YELLOW + "+-----------------------------------------+");
    }

    private static void sendFormattedMessage(Player player, BaseComponent[] line) {
        sendFormattedMessage(player, flattenComponents(line));
    }

    private static void sendFormattedMessage(Player player, TextComponent line) {
        int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText())
            .replaceAll(regex, "...."));
        if (width > MAX_LENGTH) {
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
                    .append(" ".repeat((MAX_LENGTH - width) / 4))
                    .append(".".repeat((MAX_LENGTH - width) % 4)).color(ChatColor.DARK_GRAY.asBungee())
                    .append("|").color(ChatColor.YELLOW.asBungee())
                    .create());
        }
    }

    private static void sendWrappedMessage(Player player, ArrayList<TextComponent> lines) {
        ComponentBuilder lineBuilder = new ComponentBuilder();
        int totalWidth = 0;
        for (TextComponent line : lines) {
            int width = MinecraftFont.Font.getWidth(ChatColor.stripColor(line.toPlainText().replaceAll(regex, "....")));
            if (totalWidth + width < MAX_LENGTH) {
                lineBuilder.append(line);
                totalWidth += width;
            } else {
                sendFormattedMessage(player, new TextComponent(lineBuilder.create()));
                lineBuilder = new ComponentBuilder();
                totalWidth = 0;
            }
        }
        sendFormattedMessage(player, lineBuilder.create());
    }

    /**
     * sends a wrapped empty string
     */
    private static void sendBlankLine(Player player) {
        sendFormattedMessage(player, new TextComponent(""));
    }

    private static TextComponent flattenComponents(BaseComponent[] line) {
        TextComponent component = new TextComponent();
        for (BaseComponent baseComponent : line) {
            component.addExtra(baseComponent);
        }
        return component;
    }

    /**
     * Sends a blank string to the player
     * If looking for the line wiht bars use
     * sendBLankLine()
     */
    private static void sendEmptyLine(Player player) {
        player.sendMessage("");
    }

    public record Builder(Player player) {

        public Builder blankLine() {
            MessageUtils.sendBlankLine(player);
            return this;
        }

        public Builder lineBreak() {
            MessageUtils.sendLineBreak(player);
            return this;
        }

        public Builder emptyLine() {
            MessageUtils.sendEmptyLine(player);
            return this;
        }

        public Builder formattedMessage(BaseComponent[] line) {
            MessageUtils.sendFormattedMessage(player, line);
            return this;
        }

        public Builder wrappedMessage(ArrayList<TextComponent> lines) {
            MessageUtils.sendWrappedMessage(player, lines);
            return this;
        }

        public Builder formattedMessage(TextComponent line) {
            MessageUtils.sendFormattedMessage(player, line);
            return this;
        }

        public Builder keyVal(String key, Object value) {
            MessageUtils.sendFormattedMessage(player, new TextComponent(ChatColor.AQUA + key + ": " + (value != null ? ChatColor.WHITE : ChatColor.GRAY) + value));
            return this;
        }
    }
}
