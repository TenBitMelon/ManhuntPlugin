package me.melonboy10.manhuntplugin.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TeamSelectTextMenu {

    private ArrayList<Player> invitedPlayers;
    private ArrayList<Player> runners;
    private ArrayList<Player> hunters;
    private ArrayList<Player> spectators;

    public TeamSelectTextMenu(ArrayList<Player> invitedPlayers) {

    }

    public void addPlayer(Player player) {
        invitedPlayers.add(player);
    }
}
