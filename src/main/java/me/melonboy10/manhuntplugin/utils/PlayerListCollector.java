package me.melonboy10.manhuntplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PlayerListCollector implements Collector<Player, StringBuilder, String> {

    @Override
    public Supplier<StringBuilder> supplier() {
        return StringBuilder::new;
    }

    @Override
    public BiConsumer<StringBuilder, Player> accumulator() {
        return (stringBuilder, player) -> stringBuilder.append(ChatColor.DARK_GRAY).append("- ").append(ChatColor.GRAY).append(player.getName()).append("\n");
    }

    @Override
    public BinaryOperator<StringBuilder> combiner() {
        return ((stringBuilder, stringBuilder2) -> stringBuilder.append(stringBuilder2.toString()));
    }

    @Override
    public Function<StringBuilder, String> finisher() {
        return StringBuilder::toString;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }

}
