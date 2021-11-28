package me.melonboy10.manhuntplugin.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.entity.Player;

public class SaveSeedCommand {

    @Command(name = "", aliases = "ilovethisseed", desc = "Saves the seed to a personal list.")
    public void root(@Sender Player player) {
        player.sendMessage("TODO: seed list");
    }

}
