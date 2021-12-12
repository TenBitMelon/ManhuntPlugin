package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;
import me.melonboy10.manhuntplugin.game.ManhuntGameSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class PrivacyProvider extends DrinkProvider<ManhuntGameSettings.Privacy> {

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean allowNullArgument() {
        return false;
    }

    @Nullable
    @Override
    public ManhuntGameSettings.Privacy provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        try {
            return ManhuntGameSettings.Privacy.valueOf(name);
        } catch (Exception e) {
            throw new CommandExitMessage("No privacy option for '" + name + "'.");
        }
    }

    @Override
    public String argumentDescription() {
        return "privacy";
    }

    @Override
    public List<String> getSuggestions(@NotNull String prefix) {
        return Arrays.stream(ManhuntGameSettings.Privacy.values()).map(Enum::toString).toList();
    }
}
