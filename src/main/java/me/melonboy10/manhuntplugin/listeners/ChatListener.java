package me.melonboy10.manhuntplugin.listeners;

import me.melonboy10.manhuntplugin.ManhuntPlugin;
import me.melonboy10.manhuntplugin.game.ManhuntGame;
import me.melonboy10.manhuntplugin.game.ManhuntGameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

public class ChatListener implements Listener {

    private final String[] ezReplaceMessages = {
        "Doin a bamboozle fren.",
        "I need help, teach me how to play!",
        "Let's be friends instead of fighting okay?",
        "I had something to say, then I forgot it.",
        "ILY<3",
        "I enjoy long walks on the beach and playing Hypixel",
        "Hello everyone! I'm an innocent player who loves everything Hypixel.",
        "Can you paint with all the colors of the wind",
        "When I saw the guy with a potion I knew there was trouble brewing.",
        "Sometimes I sing soppy love songs in the car.",
        "Blue is greener than purple for sure",
        "What happens if I add chocolate milk to macaroni and cheese?",
        "Anybody else really like Rick Astley?",
        "You're a great person! Do you want to play some Hypixel games with me?",
        "I love the way your hair glistens in the light.",
        "In my free time I like to watch cat videos on youtube",
        "Wait... This isn't what I typed!",
        "If the world in Minecraft is infinite....how can the sun revolve around it?",
        "When nothing is right, go left.",
        "I need help, teach me how to play!",
        "Hey Helper, how play game?",
        "Your personality shines brighter than the sun.",
        "Behold, the great and powerful, my magnificent and almighty nemesis!",
        "Please go easy on me, this is my first game!",
        "Your Clicks per second are godly. :eek:",
        "I sometimes try to say bad things and then this happens :(",
        "I like pineapple on my pizza",
        "Why can't the Ender Dragon read a book? Because he always starts at the End.",
        "I heard you like minecraft, so I built a computer so you can minecraft, while minecrafting in your minecraft.",
        "You are very good at this game friend.",
        "Pineapple doesn't go on pizza!",
        "I have really enjoyed playing with you! <3",
        "I like Minecraft pvp but you are truly better than me!",
        "Pls give me doggo memes!",
        "Maybe we can have a rematch?",
        "I like to eat pasta, do you prefer nachos?",
        "With great power... comes a great electricity bill!"
    };

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.getRecipients().clear();
        if (event.getMessage().contains("ez")) {
            event.setMessage(ezReplaceMessages[new Random().nextInt(ezReplaceMessages.length - 1)]);
        }
        if (event.getPlayer().getWorld().equals(ManhuntPlugin.hubWorld)) {
            event.getRecipients().addAll(ManhuntPlugin.hubWorld.getPlayers());
        } else {
            ManhuntGame game = ManhuntGameManager.getGame(event.getPlayer());
            if (game != null) {
                event.getRecipients().addAll(game.getPlayers().keySet());
            }
        }
    }

}
